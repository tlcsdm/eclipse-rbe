/*******************************************************************************
 * Copyright (c) 2026 Tlcsdm contributors.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Tlcsdm - initial implementation
 *******************************************************************************/
package com.tlcsdm.eclipse.rbe.ui.editor.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.tlcsdm.eclipse.rbe.RBEPlugin;
import com.tlcsdm.eclipse.rbe.model.bundle.BundleEntry;
import com.tlcsdm.eclipse.rbe.model.bundle.BundleGroup;
import com.tlcsdm.eclipse.rbe.ui.UIUtils;
import com.tlcsdm.eclipse.rbe.ui.editor.resources.ResourceManager;

/**
 * Tabular view of the resource bundle entries. Each row represents a key, and
 * each column represents a locale (the leftmost column shows the key itself).
 * Values can be edited directly in the cells; edits are propagated to the
 * underlying {@link BundleGroup}, so the rest of the editor (form view, source
 * editors, dirty state, save) react as usual.
 *
 * @author Tlcsdm
 */
public class I18nTableView extends Composite {

    private final ResourceManager resourceMediator;
    private final TableViewer tableViewer;

    public I18nTableView(Composite parent, int style,
            ResourceManager resourceMediator) {
        super(parent, style);
        this.resourceMediator = resourceMediator;
        setLayout(new FillLayout());

        tableViewer = new TableViewer(this,
                SWT.FULL_SELECTION | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        Table table = tableViewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        tableViewer.setContentProvider(ArrayContentProvider.getInstance());

        createColumns();
        refresh();
    }

    private void createColumns() {
        TableViewerColumn keyCol = new TableViewerColumn(tableViewer, SWT.NONE);
        TableColumn keyTc = keyCol.getColumn();
        keyTc.setText(RBEPlugin.getString("editor.view.table.key"));
        keyTc.setWidth(220);
        keyTc.setResizable(true);
        keyTc.setMoveable(false);
        keyCol.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return element == null ? "" : element.toString();
            }
        });

        for (Locale locale : UIUtils.sortLocales(
                resourceMediator.getLocales())) {
            createLocaleColumn(locale);
        }
    }

    private void createLocaleColumn(final Locale locale) {
        TableViewerColumn col = new TableViewerColumn(tableViewer, SWT.NONE);
        TableColumn tc = col.getColumn();
        tc.setText(UIUtils.getDisplayName(locale));
        tc.setWidth(200);
        tc.setResizable(true);
        tc.setMoveable(false);

        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (!(element instanceof String)) {
                    return "";
                }
                BundleEntry entry = resourceMediator.getBundleGroup()
                        .getBundleEntry(locale, (String) element);
                if (entry == null || entry.getValue() == null) {
                    return "";
                }
                return entry.getValue();
            }
        });

        col.setEditingSupport(new EditingSupport(tableViewer) {
            private final TextCellEditor editor = new TextCellEditor(
                    tableViewer.getTable());

            @Override
            protected boolean canEdit(Object element) {
                return element instanceof String;
            }

            @Override
            protected org.eclipse.jface.viewers.CellEditor getCellEditor(
                    Object element) {
                return editor;
            }

            @Override
            protected Object getValue(Object element) {
                BundleGroup bg = resourceMediator.getBundleGroup();
                BundleEntry entry = bg.getBundleEntry(
                        locale, (String) element);
                return (entry == null || entry.getValue() == null)
                        ? "" : entry.getValue();
            }

            @Override
            protected void setValue(Object element, Object value) {
                if (!(element instanceof String) || value == null) {
                    return;
                }
                String key = (String) element;
                String newValue = value.toString();
                BundleGroup bg = resourceMediator.getBundleGroup();
                BundleEntry existing = bg.getBundleEntry(locale, key);
                String oldValue = (existing == null
                        || existing.getValue() == null)
                                ? "" : existing.getValue();
                if (newValue.equals(oldValue)
                        && existing != null) {
                    return;
                }
                String comment = existing == null ? null
                        : existing.getComment();
                boolean commented = existing != null
                        && existing.isCommented();
                bg.addBundleEntry(locale,
                        new BundleEntry(key, newValue, comment, commented));
                tableViewer.update(key, null);
            }
        });
    }

    /**
     * Reloads keys and refreshes the displayed values.
     */
    public void refresh() {
        if (tableViewer.getTable().isDisposed()) {
            return;
        }
        List<String> keys = new ArrayList<>(
                resourceMediator.getBundleGroup().getKeys());
        tableViewer.setInput(keys);
    }

    public TableViewer getTableViewer() {
        return tableViewer;
    }

}
