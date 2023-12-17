package com.zhipin.jadehelper.ui.base.jtable.t2;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.List;
import com.intellij.openapi.module.Module;
import org.apache.commons.collections.CollectionUtils;

public class MyTable extends JTable {
//    public void init(MyTableModel model, List<Module> moduleList) {
//        this.setModel(model);
//
//        // 创建下拉框并填充模块列表
//        JComboBox<Module> comboBox = new JComboBox<>();
//        if (CollectionUtils.isNotEmpty(moduleList)) {
//            moduleList.forEach(comboBox::addItem);
//        }
//
//        TableColumn tempColumn = this.getColumnModel().getColumn(0);
//        tempColumn.setCellEditor(new DefaultCellEditor(new JTextField()));
//
//        // 设置下拉框编辑器到第二列
//        TableColumn moduleColumn = this.getColumnModel().getColumn(1);
//        moduleColumn.setCellEditor(new DefaultCellEditor(comboBox));
//
//        // 设置文本编辑器到第三列
//        TableColumn textColumn = this.getColumnModel().getColumn(2);
//        textColumn.setCellEditor(new DefaultCellEditor(new JTextField()));
//    }
}

