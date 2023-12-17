package com.zhipin.jadehelper.ui.base.jtable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class JTableModulePathModel extends DefaultTableModel {

    @Override
    public int getColumnCount() {
        return 3; // 模板类型、关联 Module、生成文件路径
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "模板类型";
            case 1: return "关联Module";
            case 2: return "生成文件路径";
            default: return null;
        }
    }


    @Override
    public boolean isCellEditable(int row, int column) {
        // 首列不需要编辑
        return column != 0;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 1) {
            return JComboBox.class;
        }
        return String.class; // 假设所有列都存储字符串数据
    }

    public int findRowByTemplateName(String templateName) {
        for (int i = 0; i < this.getRowCount(); i++) {
            if (this.getValueAt(i, 0).equals(templateName)) {
                return i;
            }
        }
        return -1; // 如果没有找到对应的行，返回 -1
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (column == 1 ) {
            System.out.println("setValueAt: " + aValue);
        }
        super.setValueAt(aValue, row, column);
        // 这里可以添加额外的逻辑，如果需要
    }
}

