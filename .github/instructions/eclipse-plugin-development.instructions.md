---
applyTo: "plugins/**,features/**,sites/**,target-platform.target,targets/**/*.target,pom.xml,.mvn/**"
---

# Eclipse plug-in development skill

Use these conventions when changing this Eclipse RBE plug-in:

- Treat `plugins/com.essiembre.eclipse.rbe` as the OSGi/PDE bundle project. Update `META-INF/MANIFEST.MF`, `plugin.xml`, `build.properties`, and localized resources together when required.
- Treat `features/com.essiembre.eclipse.rbe.feature` as the installable feature. Keep its version aligned with the bundle version and keep included plug-ins at `version="0.0.0"` unless a specific version pin is required.
- Treat `sites/com.essiembre.eclipse.rbe.site` as the p2 repository project. Update `category.xml` when feature ids or categories change.
- Do not add plug-in or feature `pom.xml` files; this repository uses Tycho pomless/polyglot discovery via `.mvn/extensions.xml`.
- Prefer target-platform changes in `targets/*.target` and keep `target-platform.target` pointing to the default target.
- Validate metadata with `mvn -B validate -ntp -DskipTests`; use `mvn -B clean verify -ntp -DskipTests` for packaging or workflow-impacting changes.
