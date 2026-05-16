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
 *     Tlcsdm - view-mode toggle and table view integration
 *******************************************************************************/
package com.tlcsdm.eclipse.rbe.ui.editor.i18n;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.FindNextAction;
import org.eclipse.ui.texteditor.FindReplaceAction;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;

import com.tlcsdm.eclipse.rbe.RBEPlugin;
import com.tlcsdm.eclipse.rbe.model.workbench.RBEPreferences;
import com.tlcsdm.eclipse.rbe.ui.editor.resources.ResourceManager;

public class I18nPageEditor extends AbstractTextEditor {

    private I18nPage i18nPage;
    private I18nTableView i18nTableView;
    private Composite viewStack;
    private StackLayout viewStackLayout;
    private Button formViewButton;
    private Button tableViewButton;
    private ResourceManager resourceMediator;

    private FindReplaceAction findReplaceAction;
    private FindNextAction findNextAction;
    private FindNextAction findPreviousAction;

    public I18nPageEditor(ResourceManager resourceMediator) {
        this.resourceMediator = resourceMediator;
    }

    public I18nPage getI18nPage() {
        return i18nPage;
    }

    @Override
    public void createPartControl(Composite parent) {
        Composite root = new Composite(parent, SWT.NONE);
        GridLayout rootLayout = new GridLayout(1, false);
        rootLayout.marginWidth = 0;
        rootLayout.marginHeight = 0;
        rootLayout.verticalSpacing = 0;
        root.setLayout(rootLayout);

        // Top bar with view-mode toggle buttons.
        Composite toolBar = new Composite(root, SWT.NONE);
        GridLayout tbLayout = new GridLayout(2, false);
        tbLayout.marginHeight = 2;
        tbLayout.marginWidth = 2;
        toolBar.setLayout(tbLayout);
        toolBar.setLayoutData(
                new GridData(SWT.FILL, SWT.TOP, true, false));

        formViewButton = new Button(toolBar, SWT.TOGGLE);
        formViewButton.setText(RBEPlugin.getString("editor.view.form"));
        formViewButton.setToolTipText(
                RBEPlugin.getString("editor.view.form.tooltip"));

        tableViewButton = new Button(toolBar, SWT.TOGGLE);
        tableViewButton.setText(RBEPlugin.getString("editor.view.table"));
        tableViewButton.setToolTipText(
                RBEPlugin.getString("editor.view.table.tooltip"));

        // Stack of the two views.
        viewStack = new Composite(root, SWT.NONE);
        viewStackLayout = new StackLayout();
        viewStack.setLayout(viewStackLayout);
        viewStack.setLayoutData(
                new GridData(SWT.FILL, SWT.FILL, true, true));

        i18nPage = new I18nPage(viewStack, SWT.NONE, resourceMediator);
        i18nTableView = new I18nTableView(
                viewStack, SWT.NONE, resourceMediator);

        SelectionAdapter switchListener = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                showTableView(e.widget == tableViewButton);
            }
        };
        formViewButton.addSelectionListener(switchListener);
        tableViewButton.addSelectionListener(switchListener);

        showTableView(RBEPreferences.getTableView());

        // Attach context-sensitive F1 help.
        String pluginId = RBEPlugin.getDefault().getBundle().getSymbolicName();
        PlatformUI.getWorkbench().getHelpSystem().setHelp(
                toolBar, pluginId + ".view_toggle");
        PlatformUI.getWorkbench().getHelpSystem().setHelp(
                i18nPage, pluginId + ".key_tree");
        PlatformUI.getWorkbench().getHelpSystem().setHelp(
                i18nTableView, pluginId + ".table_view");

        findReplaceAction = new FindReplaceAction(RBEPlugin.getDefault()
                .getResourceBundle(), null, i18nPage.getShell(),
                i18nPage.getReplaceTarget()) {
            @Override
            public void run() {
                i18nPage.findActionStart();
                super.run();
            }
        };

        findNextAction = new FindNextAction(RBEPlugin.getDefault()
                .getResourceBundle(), null, this, true) {
            @Override
            public void run() {
                i18nPage.findActionStart();
                super.run();
            }
        };
        findNextAction.setActionDefinitionId(
                IWorkbenchActionDefinitionIds.FIND_NEXT);

        findPreviousAction = new FindNextAction(RBEPlugin.getDefault()
                .getResourceBundle(), null, this, false) {
            @Override
            public void run() {
                i18nPage.findActionStart();
                super.run();
            }
        };
        findPreviousAction.setActionDefinitionId(
                IWorkbenchActionDefinitionIds.FIND_PREVIOUS);
    }

    private void showTableView(boolean tableView) {
        if (tableView) {
            viewStackLayout.topControl = i18nTableView;
            i18nTableView.refresh();
        } else {
            viewStackLayout.topControl = i18nPage;
        }
        formViewButton.setSelection(!tableView);
        tableViewButton.setSelection(tableView);
        viewStack.layout();
        RBEPreferences.setTableView(tableView);
    }

    /**
     * Refreshes whichever view is currently active. Called when the model
     * changes (e.g. files reloaded, new locale added).
     */
    public void refreshActiveView() {
        if (i18nTableView != null && !i18nTableView.isDisposed()) {
            i18nTableView.refresh();
        }
        if (i18nPage != null && !i18nPage.isDisposed()) {
            i18nPage.refreshTextBoxes();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAdapter(Class<T> required) {
        if (required.equals(IFindReplaceTarget.class)) {
            return (T) i18nPage.getReplaceTarget();
        }
        try {
            return super.getAdapter(required);
        } catch (NullPointerException e) {
            // AbstractTextEditor is not fully initialised in this context
            // (no document/editor-input), so adapter factories from other
            // plugins (e.g. SDBG) may throw NPE when they inspect the
            // editor state.  Downgrade to WARNING to avoid cluttering the
            // Eclipse error log with misleading ERROR entries.
            RBEPlugin.getDefault().getLog().log(new Status(
                    Status.WARNING, RBEPlugin.ID, 
                    "Got a NPE from AbstractTextEditor#getAdapter(Class<T>) "
                  + "for adapter class: " + required, e));
            return null;
        }
    }

    public IAction getFindReplaceAction() {
        return findReplaceAction;
    }

    public IAction getFindNextAction() {
        return findNextAction;
    }

    public IAction getFindPreviousAction() {
        return findPreviousAction;
    }
}
