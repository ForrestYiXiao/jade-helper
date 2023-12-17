package com.zhipin.jadehelper.ui.base.jtable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class JTableCustomRenderer extends DefaultTableCellRenderer {
    private final JTableCustomerEditor editor;

    public JTableCustomRenderer(JTableCustomerEditor editor) {
        this.editor = editor;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (column == 1) {
            // 第二列使用 JComboBox
            return editor.getComboBoxForRow(row);
        } else {
            // 其他列使用默认渲染器
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}
