package com.zhipin.jadehelper.ui.base;//package com.zhipin.jadehelper.ui.base;
//
//import com.intellij.openapi.module.Module;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author Zhang Yixiao
// * @date 2023/10/29 10:47
// */
//public class JTableCustomerEditor extends DefaultCellEditor {
//    private JTextField templateTextField;
//
//    private JTextField textField;
//
//    private List<Module> moduleList;
//    private Map<Integer, JComboBox<String>> comboBoxMap;
//
//    public JTableCustomerEditor(List<Module> moduleList) {
//        super(new JTextField());
//        this.textField = (JTextField) getComponent();
//        this.templateTextField = (JTextField) getComponent();
//        this.comboBoxMap = new HashMap<>();
//        this.moduleList = moduleList;
//
////        // 假设有 10 行
////        for (int i = 0; i < 10; i++) {
////            JComboBox<String> comboBox = new JComboBox<>();
////            comboBox.addItem(i + "Module1");
////            comboBox.addItem(i + "Module2");
////            comboBoxMap.put(i, comboBox);
////        }
//        comboBoxMap.forEach((k, v) -> {
//            moduleList.forEach(module -> {
//                v.addItem(module.getName());
//            });
//        });
//
//        this.setClickCountToStart(1);
//    }
//
//    public JComboBox<String> getComboBoxForRow(int row) {
//        if (comboBoxMap.get(row) == null) {
//            JComboBox<String> comboBox = new JComboBox<>();
//            moduleList.forEach(module -> {
//                comboBox.addItem(module.getName());
//            });
//            comboBoxMap.put(row, comboBox);
//        }
//        return comboBoxMap.get(row);
//    }
//
//    @Override
//    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//        if (column == 1) {
//            JComboBox<String> comboBox = getComboBoxForRow(row);
//            comboBox.setSelectedItem(value);
//            this.editorComponent = comboBox; // 设置当前的编辑器组件
//            return comboBox;
//        } else if (column == 2) {
//            textField.setText((String) value);
//            return textField;
//        } else {
//            templateTextField.setText((String) value);
//            return templateTextField;
//        }
//
////        if (column == 1) {
////            comboBoxMap.get(row).setSelectedItem(value);
////            return comboBoxMap.get(row);
////        } else if(column == 2) {
////            textField.setText((String) value);
////            return textField;
////        }else {
////            templateTextField.setText((String) value);
////            return templateTextField;
////        }
//    }
//
//    //    @Override
////    public Object getCellEditorValue() {
////        if (this.editorComponent instanceof JComboBox) {
////            return ((JComboBox) this.editorComponent).getSelectedItem();
////        } else {
////            Object value = delegate.getCellEditorValue();
////            return textField.getText();
////        }
////    }
//
//    @Override
//    public Object getCellEditorValue() {
//        if (this.editorComponent instanceof JComboBox) {
//            // Return the selected item for JComboBox
//            return ((JComboBox<?>) this.editorComponent).getSelectedItem();
//        } else if (this.editorComponent instanceof JTextField) {
//            // Return the text for JTextField
//            return ((JTextField) this.editorComponent).getText();
//        }
//        return super.getCellEditorValue(); // Fallback to the default editor value
//    }
//
//}
