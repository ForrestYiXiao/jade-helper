package com.zhipin.jadehelper.ui.base;//package com.zhipin.jadehelper.ui.base;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableCellRenderer;
//import java.awt.*;
//
///**
// * @author Zhang Yixiao
// * @date 2023/10/29 10:48
// */
//public class JTableCustomRenderer extends DefaultTableCellRenderer {
//    private JPanel panel;
//    private JLabel label;
//    private JTextField textField;
//    private JTableCustomerEditor editor;
//
//    public JTableCustomRenderer(JTableCustomerEditor editor) {
//        super();
//        this.editor = editor;
//        panel = new JPanel();
//        label = new JLabel();
//        textField = new JTextField();
//    }
//
//    @Override
//    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        panel.removeAll();
//
//        if (column == 1) {
////            label.setText("Module:");
////            panel.add(label);
//            JComboBox<String> comboBox = editor.getComboBoxForRow(row);
//            if (comboBox != null) {
//                if (value == null) {
//                    comboBox.setSelectedIndex(0);
//                } else {
//                    comboBox.setSelectedItem(value);
//                }
//                panel.add(comboBox);
//            }
//        } else if (column == 2) {
////            label.setText("Path:");
////            panel.add(label);
//            textField.setText((String) value);
//            panel.add(textField);
//        } else if (column == 0) {
//            panel.add(new JTextField((String) value));
//        }
//        return panel;
//    }
//}
