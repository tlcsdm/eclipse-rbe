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
package com.tlcsdm.eclipse.rbe.ui.editor.i18n.tree;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import com.tlcsdm.eclipse.rbe.model.tree.KeyTreeItem;
import com.tlcsdm.eclipse.rbe.model.tree.visitors.IsCommentedVisitor;
import com.tlcsdm.eclipse.rbe.ui.UIUtils;

/**
 * Label provider for key tree viewer.
 * @author Pascal Essiembre
 * @author Tobias Langner
 */
public class KeyTreeLabelProvider 
        extends LabelProvider implements IFontProvider, IColorProvider {    
    
//    private Color colorInactive = UIUtils.getSystemColor(SWT.COLOR_GRAY);
    private Color colorCommented = UIUtils.getSystemColor(SWT.COLOR_GRAY);

    /** Group font. */
    private Font keyFont = UIUtils.createFont(SWT.NORMAL);
    private Font groupFontKey = UIUtils.createFont(SWT.NORMAL);
    private Font groupFontNoKey = UIUtils.createFont(SWT.NORMAL);

    
    /**
     * @see ILabelProvider#getImage(Object)
     */
    public Image getImage(Object element) {
        // Return null to avoid adding images to the SWT shared ImageList on
        // Windows, which has a hard limit of 64 entries. Eclipse's own UI can
        // fill most of those slots before this tree is created, causing an
        // ArrayIndexOutOfBoundsException. Key status is conveyed through color
        // and font instead.
        return null;
    }

    @Override
    public String getText(Object element) {
        return ((KeyTreeItem) element).getName(); 
    }

    @Override
    public void dispose() {
        groupFontKey.dispose();
        groupFontNoKey.dispose();
        keyFont.dispose();
//        colorCommented.dispose();
    }

    @Override
    public Font getFont(Object element) {
        KeyTreeItem item = (KeyTreeItem) element; 
        if (item.getChildren().size() > 0) {
            if (item.getKeyTree().getBundleGroup().isKey(item.getId())) {
                return groupFontKey;
            }
            return groupFontNoKey;
        }
        return keyFont;
    }

    @Override
    public Color getForeground(Object element) {
        KeyTreeItem treeItem = (KeyTreeItem) element; 
//        // No key
//        if (!treeItem.getKeyTree().getBundleGroup().isKey(treeItem.getId())) {
//            return colorInactive;
//        }

        // Commented
        IsCommentedVisitor commentedVisitor = new IsCommentedVisitor();
        treeItem.accept(commentedVisitor, null);
        if (commentedVisitor.hasOneCommented()) {
            return colorCommented;
        }

        return null;
    }

    @Override
    public Color getBackground(Object element) {
        // TODO Auto-generated method stub
        return null;
    }
}
