package com.zhipin.jadehelper.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.zhipin.jadehelper.constants.MainEntryConstants;
import com.zhipin.jadehelper.service.TableInfoSettingsService;
import com.zhipin.jadehelper.tool.CacheDataUtils;
import com.zhipin.jadehelper.tool.SqlUtils;
import com.zhipin.jadehelper.tool.StringUtils;
import com.zhipin.jadehelper.ui.SelectSavePath;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;

/**
 * @author Zhang Yixiao
 * @date 2023/5/25 20:49
 */
public class JadeHelperMainAction extends AnAction {

    Log log = LogFactory.getLog(JadeHelperMainAction.class);

    private TableInfoSettingsService tableInfoService;
    public JadeHelperMainAction() {
        tableInfoService = TableInfoSettingsService.getInstance();
    }

    private CacheDataUtils cacheDataUtils = CacheDataUtils.getInstance();

    @Override
    public void actionPerformed(AnActionEvent e) {

        // 创建文本域，设置行数和列数
        JTextArea textArea = new JTextArea(30, 80);
        // 设置自动换行
        textArea.setLineWrap(false);
        // 设置单词间换行
        textArea.setWrapStyleWord(true);

        // 将文本域放入滚动面板
        JScrollPane scrollPane = new JScrollPane(textArea);
//        scrollPane.setSize(600, 800);

        int result = JOptionPane.showConfirmDialog(null, scrollPane, "请输入建表SQL:",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            doMainAction(e, textArea);
        } else {
            System.out.println("用户取消了输入。");
        }
//        try (Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.1.31:3306/test", "boss", "mVsIGxpYmV2Z");
//             Statement stmt = conn.createStatement()) {
////            stmt.execute(sql);
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "SQL执行错误: " + ex.getMessage());
//        }

    }

    private void doMainAction(AnActionEvent e, JTextArea textArea) {
        // 获取用户输入的文本
        String sql = textArea.getText();
        if (StringUtils.isEmpty(sql)) {
            JOptionPane.showMessageDialog(null, "请输入SQL!");
            return;
        }
        try {
            cacheDataUtils.setStatement(CCJSqlParserUtil.parse(SqlUtils.formatSql(sql)));
        } catch (JSQLParserException ex) {
//            throw new RuntimeException(ex);
            log.error("SQL解析错误: " + ex.getMessage(), ex);
            JOptionPane.showMessageDialog(null, "SQL 解析失败，请输入Create Table SQL !");
        }
//        TableDetail tableDetail = SqlUtils.parseCreateTableSql(SqlUtils.formatSql(sql));
//        System.out.println("Table Name: " + tableDetail.getTableName());
//        for (TableDetail.ColumnDetail column : tableDetail.getColumns()) {
//            System.out.println("Column: " + column.getColumnName() + ", Type: " + column.getDataType());
//        }

        //JOptionPane.showMessageDialog(null, "SQL执行错误: " + ex.getMessage());

        //获取链接

        //失败则跳过

        //检测SQL是否正常执行

        //SQL优化

        //下一步
        new SelectSavePath(e.getProject(), MainEntryConstants.SELECT_PATH_INPUT).show();
        System.out.println("输入的建表SQL是: " + sql);
    }

    //DDL SQL字符串解析

}
