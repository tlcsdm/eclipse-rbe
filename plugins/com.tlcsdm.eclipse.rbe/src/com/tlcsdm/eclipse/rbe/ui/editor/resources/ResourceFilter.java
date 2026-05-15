/*
 * Copyright (C) 2003-2014  Pascal Essiembre
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tlcsdm.eclipse.rbe.ui.editor.resources;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.tlcsdm.eclipse.rbe.RBEPlugin;
import com.tlcsdm.eclipse.rbe.model.workbench.RBEPreferences;

/**
 * Filters resource bundle files by locale based on a preference-configured glob pattern list.
 * <p>
 * A comma/semicolon/space-separated list of glob patterns (using {@code *} and {@code ?}
 * wildcards) controls which locale files are opened in the editor. The default value {@code "*"}
 * means no filtering (all locales are shown). The root locale (the base properties file without
 * any locale suffix) is always included regardless of the filter.
 * </p>
 * <p>
 * Some parts of this implementation are based on org.eclipse.babel.editor.* files and
 * the upstream PR https://github.com/essiembre/eclipse-rbe/pull/61 by wolfgang-ch.
 * </p>
 *
 * @author wolfgang-ch (original concept)
 * @author tlcsdm (adaptation)
 */
public class ResourceFilter implements IPropertyChangeListener {

    private static final IPreferenceStore PREFS = RBEPlugin.getDefault().getPreferenceStore();

    /**
     * The root locale used for the original properties file (the file without any locale suffix).
     */
    private static final Locale ROOT_LOCALE = new Locale(""); //$NON-NLS-1$

    private static final Pattern COUNTRY_MATCHER =
            Pattern.compile("_([a-z]{2,3})_([A-Z]{2})"); //$NON-NLS-1$
    private static final Pattern VARIANT_MATCHER =
            Pattern.compile("_([a-z]{2,3})_([A-Z]{2})_(\\w*)"); //$NON-NLS-1$

    /** Cached compiled glob patterns; {@code null} means the cache is stale. */
    private static Pattern[] cachedCompiledLocaleFilter;

    private static final ResourceFilter INSTANCE = new ResourceFilter();

    /**
     * Returns the singleton {@link IPropertyChangeListener} that keeps the compiled-pattern
     * cache in sync with preference store changes.
     *
     * @return the singleton instance
     */
    public static IPropertyChangeListener getInstance() {
        return INSTANCE;
    }

    /**
     * Invalidates the compiled-pattern cache when the preference value changes.
     */
    private static void onLocaleFilterChange() {
        cachedCompiledLocaleFilter = null;
    }

    /**
     * Converts a simple glob pattern ({@code *} and {@code ?} wildcards) to a case-insensitive
     * {@link Pattern}.
     *
     * @param glob the glob expression
     * @return the compiled {@link Pattern}
     */
    private static Pattern compileGlob(String glob) {
        StringBuilder sb = new StringBuilder("(?i)"); //$NON-NLS-1$
        for (int i = 0; i < glob.length(); i++) {
            char c = glob.charAt(i);
            switch (c) {
                case '*':
                    sb.append(".*"); //$NON-NLS-1$
                    break;
                case '?':
                    sb.append('.'); //$NON-NLS-1$
                    break;
                // Escape regex meta-characters that may appear in user input
                case '.': case '\\': case '[': case ']':
                case '(': case ')': case '{': case '}':
                case '^': case '$': case '+': case '|':
                    sb.append('\\');
                    sb.append(c);
                    break;
                default:
                    sb.append(c);
            }
        }
        return Pattern.compile(sb.toString());
    }

    /**
     * Returns the cached array of compiled glob {@link Pattern}s derived from the preference
     * value. The array is recompiled lazily whenever the cache has been invalidated.
     *
     * @return compiled patterns, never {@code null}
     */
    private static synchronized Pattern[] getFilterLocalesPatterns() {
        if (cachedCompiledLocaleFilter != null) {
            return cachedCompiledLocaleFilter;
        }

        String pref = PREFS.getString(RBEPreferences.FILTER_LOCALES_STRING_MATCHERS);
        StringTokenizer tokenizer = new StringTokenizer(pref, ";, ", false); //$NON-NLS-1$

        cachedCompiledLocaleFilter = new Pattern[tokenizer.countTokens()];
        int i = 0;
        while (tokenizer.hasMoreTokens()) {
            cachedCompiledLocaleFilter[i++] = compileGlob(tokenizer.nextToken().trim());
        }
        return cachedCompiledLocaleFilter;
    }

    /**
     * Filters the given array of locales against the configured glob patterns.
     * <p>
     * The root locale is always included. Non-root locales are included in the order defined by
     * the pattern list.
     * </p>
     *
     * @param locales locales to filter
     * @return filtered locales; may be empty but never {@code null}
     */
    private Locale[] filterLocales(Locale[] locales) {
        Pattern[] patterns = getFilterLocalesPatterns();
        Set<Locale> already = new HashSet<>();
        ArrayList<Locale> result = new ArrayList<>();

        // The root locale (base file) always passes through.
        for (int j = 0; j < locales.length; j++) {
            Locale loc = locales[j];
            if (ROOT_LOCALE.equals(loc) || loc == null) {
                already.add(loc);
                result.add(loc);
                break;
            }
        }

        // Apply each pattern in order to collect matching locales.
        for (int pi = 0; pi < patterns.length; pi++) {
            Pattern pattern = patterns[pi];
            for (int j = 0; j < locales.length; j++) {
                Locale loc = locales[j];
                if (!already.contains(loc) && pattern.matcher(loc.toString()).matches()) {
                    already.add(loc);
                    result.add(loc);
                    if (already.size() == locales.length) {
                        return locales; // fast path: all locales matched
                    }
                }
            }
        }

        return result.toArray(new Locale[0]);
    }

    /**
     * Determines whether the resource with the given name should be displayed in the editor.
     * <p>
     * The {@code regex} parameter must be the expression produced by
     * {@link ResourceFactory#getPropertiesFileRegEx(org.eclipse.core.resources.IResource)},
     * whose capture groups are:
     * <ol>
     *   <li>bundle name</li>
     *   <li>full locale suffix (may be {@code null})</li>
     *   <li>{@code _language} only (may be {@code null})</li>
     *   <li>{@code _language_COUNTRY} (may be {@code null})</li>
     *   <li>{@code _language_COUNTRY_variant} (may be {@code null})</li>
     *   <li>file extension</li>
     * </ol>
     * </p>
     *
     * @param resourceName the file name to test
     * @param regex        the properties-file regular expression with the groups listed above
     * @return {@code true} if the resource should be displayed
     */
    public boolean isResourceDisplayed(String resourceName, String regex) {
        Matcher resourceMatcher = Pattern.compile(regex).matcher(resourceName);
        // The calling method already verified the name matches; invoke matches() to populate groups.
        resourceMatcher.matches();

        String localeLanguage    = resourceMatcher.group(3);
        String localeWithCountry = resourceMatcher.group(4);
        String localeWithVariant = resourceMatcher.group(5);

        Locale resourceLocale;

        if (localeLanguage == null && localeWithCountry == null && localeWithVariant == null) {
            // Base file — root locale
            resourceLocale = ROOT_LOCALE;

        } else if (localeLanguage != null) {
            // Language only, e.g. "_de" -> "de"
            resourceLocale = new Locale(localeLanguage.substring(1));

        } else if (localeWithCountry != null) {
            // Language + country, e.g. "_de_DE"
            Matcher cm = COUNTRY_MATCHER.matcher(localeWithCountry);
            cm.matches();
            resourceLocale = new Locale(cm.group(1), cm.group(2));

        } else {
            // Language + country + variant, e.g. "_de_DE_var"
            Matcher vm = VARIANT_MATCHER.matcher(localeWithVariant);
            if (!vm.matches()) {
                return false;
            }
            resourceLocale = new Locale(vm.group(1), vm.group(2), vm.group(3));
        }

        return filterLocales(new Locale[] { resourceLocale }).length > 0;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (RBEPreferences.FILTER_LOCALES_STRING_MATCHERS.equals(event.getProperty())) {
            onLocaleFilterChange();
        }
    }
}
