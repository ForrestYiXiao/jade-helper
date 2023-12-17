package com.zhipin.jadehelper.ui.component;

import com.intellij.ide.actions.runAnything.RunAnythingChooseContextAction;

import javax.swing.*;
import java.awt.*;

/**
 * 模板选择组件
 *
 * @author Zhang Yixiao
 * @version 1.0.0
 * @date 2021/08/16 16:18
 */
public class GenerateListComponent extends JPanel implements ListCellRenderer<RunAnythingChooseContextAction.ModuleItem> {
    private JLabel moduleLabel = new JLabel("");
    private JComboBox<String> moduleComboBox = new JComboBox<>();
    private JLabel pathLabel = new JLabel("Path:");
    private JTextField pathField = new JTextField();

    public GenerateListComponent() {
        setLayout(new GridLayout(2, 2));
        add(moduleLabel);
        add(moduleComboBox);
        add(pathLabel);
        add(pathField);
    }

//    @Override
//    public Component getListCellRendererComponent(JList<? extends ModuleItem> list, ModuleItem value, int index, boolean isSelected, boolean cellHasFocus) {
//        moduleComboBox.setSelectedItem(value.getModuleName());
//        pathField.setText(value.getPath());
//        return this;
//    }

    @Override
    public Component getListCellRendererComponent(JList<? extends RunAnythingChooseContextAction.ModuleItem> list, RunAnythingChooseContextAction.ModuleItem value, int index, boolean isSelected, boolean cellHasFocus) {
        moduleComboBox.setSelectedItem(value);
        pathField.setText("value");
        return this;
    }
}
