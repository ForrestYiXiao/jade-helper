package com.zhipin.jadehelper.action;

import javax.swing.*;

public class ActionTest {

    public ActionTest() {
        // 创建文本域，设置行数和列数
        JTextArea textArea = new JTextArea(300, 200);
        // 设置自动换行
        textArea.setLineWrap(false);
        // 设置单词间换行
        textArea.setWrapStyleWord(true);

        // 将文本域放入滚动面板
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setSize(600, 800);

        int result = JOptionPane.showConfirmDialog(null, scrollPane, "请输入建表SQL:",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String sql = textArea.getText();  // 获取用户输入的文本
            System.out.println("输入的建表SQL是: " + sql);
        } else {
            System.out.println("用户取消了输入。");
        }

    }

    public static void main(String[] args) {
        new ActionTest();
    }
}
