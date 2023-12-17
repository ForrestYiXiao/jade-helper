package com.zhipin.jadehelper.ui.base.jtable;

import com.intellij.openapi.module.Module;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class JTableCustomerEditor extends DefaultCellEditor {
    private final JTextField textField;
    private final Map<Integer, JComboBox<String>> comboBoxMap;
    private final List<Module> moduleList;

    public JTableCustomerEditor(List<Module> moduleList) {
        super(new JTextField());
        this.textField = (JTextField) getComponent();
        this.moduleList = moduleList;
        this.comboBoxMap = new HashMap<>();

        this.setClickCountToStart(1);
    }

    public JComboBox<String> getComboBoxForRow(int row) {
        return comboBoxMap.computeIfAbsent(row, k -> createComboBox());
    }

    private JComboBox<String> createComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        moduleList.forEach(module -> {
            comboBox.addItem(module.getName());
        });
        return comboBox;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (column == 1) {
            JComboBox<String> comboBox = getComboBoxForRow(row);
            comboBox.setSelectedItem(value);
            return comboBox;
        } else {
            textField.setText((String) value);
            return textField;
        }
    }

    @Override
    public Object getCellEditorValue() {
        if (editorComponent instanceof JComboBox) {
            return ((JComboBox<?>) editorComponent).getSelectedItem();
        } else {
            return textField.getText();
        }
    }
}

