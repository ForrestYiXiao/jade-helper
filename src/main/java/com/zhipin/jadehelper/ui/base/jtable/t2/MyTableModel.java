package com.zhipin.jadehelper.ui.base.jtable.t2;

import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel {
    // 定义列名
    String[] columnNames = {"模板", "关联Module", "生成路径"};

    public MyTableModel() {
        super.setColumnIdentifiers(columnNames);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // 首列不需要编辑
        return column != 0;
    }

    public int findRowByTemplateName(String templateName) {
        for (int i = 0; i < this.getRowCount(); i++) {
            if (this.getValueAt(i, 0).equals(templateName)) {
                return i;
            }
        }
        return -1; // 如果没有找到对应的行，返回 -1
    }
}
