package com.zhipin.jadehelper.ui.base;//package com.zhipin.jadehelper.ui.base;
//
//import javax.swing.table.DefaultTableModel;
//
///**
// * @author Zhang Yixiao
// * @date 2023/11/5 17:33
// */
//public class JTableModulePathModel extends DefaultTableModel {
//
//    @Override
//    public int getColumnCount() {
//        return 3;
//    }
//
//    @Override
//    public String getColumnName(int column) {
//        switch (column) {
//            case 0:
//                return "模板类型";
//            case 1:
//                return "关联Module";
//            case 2:
//                return "生成文件路径";
//            default:
//                return "操作";
//        }
//    }
//
//    @Override
//    public boolean isCellEditable(int row, int column) {
//        // 首列不需要编辑
//        return column != 0;
//    }
//
//    // 添加一个方法来根据模板类型名称查找行索引
//    public int findRowByTemplateName(String templateName) {
//        for (int i = 0; i < this.getRowCount(); i++) {
//            if (this.getValueAt(i, 0).equals(templateName)) {
//                return i;
//            }
//        }
//        return -1; // 如果没有找到对应的行，返回 -1
//    }
//
//}
