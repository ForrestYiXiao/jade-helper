package com.zhipin.jadehelper.service.test;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhang Yixiao
 */
public class TestFrame {

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("TestFrame");
        jFrame.setSize(500, 500);
        // 设置默认关闭操作
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public String getColumnName(int column) {
                return column == 0 ? "Module" : "Path";
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // 假设您想让所有单元格都是可编辑的
                return true;
            }
        };
        JPanel generateCodePanel = new JPanel(new BorderLayout(), true);
        JTable generateList = new JTable(model);

        JPanel pathPanel = new JPanel(new BorderLayout());
        pathPanel.add(new JButton("Path"), BorderLayout.CENTER);
        JPanel modulePanel = new JPanel(new BorderLayout());
        modulePanel.add(new JButton("module"), BorderLayout.CENTER);
//        model.addRow(new JButton[]{new JButton("module"), new JButton("Path")});
//        model.addRow(new JPanel[]{modulePanel, pathPanel});

        model.addRow(new Object[]{"Module1", "Path1"});
        model.addRow(new Object[]{"Module2", "Path2"});

// 设置自定义渲染器和编辑器
        generateList.setRowHeight(30);
        CustomEditor customEditor = new CustomEditor();
        generateList.setDefaultRenderer(Object.class, new CustomRenderer(customEditor));
        generateList.setDefaultEditor(Object.class, customEditor);
        generateList.setSurrendersFocusOnKeystroke(true);
//        // 添加一个滚动面板
        JScrollPane scrollPane = new JScrollPane(generateList);
        generateCodePanel.add(scrollPane, BorderLayout.CENTER);
        generateCodePanel.add(modulePanel, BorderLayout.NORTH);
//        // 将面板添加到窗口中
        jFrame.add(generateCodePanel);
        jFrame.pack();
        jFrame.setVisible(true);
    }


    static class CustomRenderer extends DefaultTableCellRenderer {
        private JPanel panel;
        private JLabel label;
        private JTextField textField;
        private CustomEditor editor;

        public CustomRenderer(CustomEditor editor) {
            super();
            this.editor = editor;
            panel = new JPanel();
            label = new JLabel();
            textField = new JTextField();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            panel.removeAll();
            if (column == 0) {
                label.setText("");
                panel.add(label);
                JComboBox<String> comboBox = editor.getComboBoxForRow(row);
                comboBox.setSelectedItem(value);
                panel.add(comboBox);
            } else {
                label.setText("Path:");
                panel.add(label);
                textField.setText((String) value);
                panel.add(textField);
            }
            return panel;
        }
    }


    static class CustomEditor extends DefaultCellEditor {
        private JTextField textField;
        private Map<Integer, JComboBox<String>> comboBoxMap;

        public CustomEditor() {
            super(new JTextField());
            this.textField = (JTextField) getComponent();
            this.comboBoxMap = new HashMap<>();

            // 假设有 10 行
            for (int i = 0; i < 10; i++) {
                JComboBox<String> comboBox = new JComboBox<>();
                comboBox.addItem(i + "Module1");
                comboBox.addItem(i + "Module2");
                comboBoxMap.put(i, comboBox);
            }

            this.setClickCountToStart(1);
        }

        public JComboBox<String> getComboBoxForRow(int row) {
            return comboBoxMap.get(row);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (column == 0) {
                comboBoxMap.get(row).setSelectedItem(value);
                return comboBoxMap.get(row);
            } else {
                textField.setText((String) value);
                return textField;
            }
        }

        @Override
        public Object getCellEditorValue() {
            if (this.editorComponent instanceof JComboBox) {
                return ((JComboBox) this.editorComponent).getSelectedItem();
            } else {
                return textField.getText();
            }
        }
    }
}
