package com.zhipin.jadehelper.ui.component;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBCheckBox;
import com.zhipin.jadehelper.entity.Template;
import com.zhipin.jadehelper.entity.TemplateGroup;
import com.zhipin.jadehelper.service.SettingsStorageService;
import com.zhipin.jadehelper.tool.StringUtils;
import com.zhipin.jadehelper.ui.base.jtable.JTableModulePathModel;
import com.zhipin.jadehelper.ui.base.jtable.t2.MyTableModel;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模板选择组件
 *
 * @author Zhang Yixiao
 * @version 1.0.0
 */
public class TemplateSelectComponent {
    @Getter
    private JPanel mainPanel;

    /**
     * 分组
     */
    @Getter
    private ComboBox<String> groupComboBox;

    /**
     * 选中所有复选框
     */
    private JBCheckBox allCheckbox;

    /**
     * 所有复选框
     */
    private List<JBCheckBox> checkBoxList;

    /**
     * 模板面板
     */
    private JPanel templatePanel;

    //    private JTableModulePathModel model;
    private MyTableModel model;
    private List<Module> moduleList;

    private JTextField packageField;

    public TemplateSelectComponent(MyTableModel model, List<Module> moduleList, JTextField packageField) {
        this.model = model;
        this.moduleList = moduleList;
        this.packageField = packageField;
        this.init();
    }

    public MyTableModel getModel() {
        return model;
    }

    private void init() {
        this.mainPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        this.groupComboBox = new ComboBox<>();
        this.groupComboBox.setSwingPopup(false);
        this.groupComboBox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String groupName = (String) groupComboBox.getSelectedItem();
                if (StringUtils.isEmpty(groupName)) {
                    return;
                }
                refreshTemplatePanel(groupName);
            }
        });
        this.allCheckbox = new JBCheckBox("全选");
        this.allCheckbox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkBoxList == null) {
                    return;
                }
                for (JBCheckBox checkBox : checkBoxList) {
                    checkBox.setSelected(allCheckbox.isSelected());
                    if (checkBox.isSelected()) {
                        // 复选框被选中
                        if (model.findRowByTemplateName(checkBox.getText()) == -1) {
                            model.addRow(new Object[]{checkBox.getText(), moduleList.get(0), "src/main/java/" + packageField.getText().replace(".", "/")});
                        }
                    } else {
                        // 复选框未被选中
                        int selectedRow = model.findRowByTemplateName(checkBox.getText());
                        if (selectedRow != -1) {
                            model.removeRow(selectedRow);
                        }
                        System.out.println("remove select row ." + selectedRow);
                    }
                }
                System.out.println("allCheckbox = " + allCheckbox);
            }
        });
        topPanel.add(this.groupComboBox, BorderLayout.WEST);
        topPanel.add(this.allCheckbox, BorderLayout.EAST);
        this.mainPanel.add(topPanel, BorderLayout.NORTH);
        this.templatePanel = new JPanel(new GridLayout(-1, 2));
        this.mainPanel.add(templatePanel, BorderLayout.CENTER);
        this.refreshData();
    }

    private void refreshData() {
        this.groupComboBox.removeAllItems();
        for (String groupName : SettingsStorageService.getSettingsStorage().getTemplateGroupMap().keySet()) {
            this.groupComboBox.addItem(groupName);
        }
    }

    private void refreshTemplatePanel(String groupName) {
        this.allCheckbox.setSelected(false);
        this.templatePanel.removeAll();
        this.checkBoxList = new ArrayList<>();
        TemplateGroup templateGroup = SettingsStorageService.getSettingsStorage().getTemplateGroupMap().get(groupName);
        for (Template template : templateGroup.getElementList()) {
            JBCheckBox checkBox = new JBCheckBox(template.getName());
            checkBox.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("checkBox = " + checkBox);
                    JBCheckBox sourceCheckBox = (JBCheckBox) e.getSource();

                    if (sourceCheckBox.isSelected()) {
                        // 复选框被选中
                        model.addRow(new Object[]{template.getName(), moduleList.get(0), "src/main/java/" + packageField.getText().replace(".", "/")});
                    } else {
                        // 复选框未被选中
                        int selectedRow = model.findRowByTemplateName(template.getName());
                        if (selectedRow != -1) {
                            model.removeRow(selectedRow);
                        }
                        System.out.println("remove select row ." + selectedRow);
                    }
                }
            });
            this.checkBoxList.add(checkBox);
            this.templatePanel.add(checkBox);
        }
        this.mainPanel.updateUI();
    }


    public String getSelectedGroupName() {
        return (String) this.groupComboBox.getSelectedItem();
    }

    public void setSelectedGroupName(String groupName) {
        this.groupComboBox.setSelectedItem(groupName);
    }

    public List<Template> getAllSelectedTemplate() {
        for (Vector vector : this.model.getDataVector()) {
            System.out.println("vector = " + vector);
        }
        String groupName = (String) this.groupComboBox.getSelectedItem();
        if (StringUtils.isEmpty(groupName)) {
            return Collections.emptyList();
        }
        TemplateGroup templateGroup = SettingsStorageService.getSettingsStorage().getTemplateGroupMap().get(groupName);
        Map<String, Template> map = templateGroup.getElementList().stream().collect(Collectors.toMap(Template::getName, val -> val));
        List<Template> result = new ArrayList<>();
        for (JBCheckBox checkBox : this.checkBoxList) {
            if (checkBox.isSelected()) {
                Template template = map.get(checkBox.getText());
                if (template != null) {
                    result.add(template);
                }
            }
        }
        return result;
    }
}
