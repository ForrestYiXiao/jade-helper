package com.zhipin.jadehelper.tool;

import com.zhipin.jadehelper.entity.ColumnInfo;
import com.zhipin.jadehelper.entity.TableInfo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.jsqlparser.JSQLParserException;

/**
 * @author Zhang Yixiao
 * @date 2023/10/29 17:17
 */
public class SqlUtils {
    Logger log = Logger.getLogger("SqlUtils");

    // DDL SQL字符串解析, 打印表名字段注释等信息
    public static String formatSql(String sql) {
        // 1. 去除换行符
        sql = sql.replaceAll("\n", " ");
        // 2. 去除多余空格
        sql = sql.replaceAll("\\s+", " ");
        // 3. 去除单引号
//        sql = sql.replaceAll("'", "");
        // 4. 去除反引号
        sql = sql.replaceAll("`", "");

        return sql;
    }

    public static TableDetail parseCreateTableSql1(String sql) {
        CreateTable createTable = null;
        try {
            createTable = (CreateTable) CCJSqlParserUtil.parse(sql);
            TableDetail tableDetail = new TableDetail();

            // 设置表名
            tableDetail.setTableName(createTable.getTable().getName());

            // 遍历并提取列定义
            for (ColumnDefinition columnDefinition : createTable.getColumnDefinitions()) {
                Column column = new Column(createTable.getTable(), columnDefinition.getColumnName());
                ColDataType colDataType = columnDefinition.getColDataType();
                String columnName = column.getColumnName();
                String dataType = colDataType.getDataType();
                String comment = extractComment(columnDefinition);
                tableDetail.addColumn(columnName, dataType);
            }

            return tableDetail;
        } catch (JSQLParserException e) {
            return null;
        }
    }

    public static TableInfo parseCreateTableSql(String sql) {
        CreateTable createTable = null;
        try {
            createTable = (CreateTable) CCJSqlParserUtil.parse(sql);
            TableInfo tableInfo = new TableInfo();
            tableInfo.setStatement(createTable);

            // 设置表名
            tableInfo.setName(createTable.getTable().getName());
            List<ColumnInfo> columnInfoList = new ArrayList<>();

            tableInfo.setFullColumn(new ArrayList<>());
            tableInfo.setPkColumn(new ArrayList<>());
            tableInfo.setOtherColumn(new ArrayList<>());
            // 遍历并提取列定义
            for (ColumnDefinition columnDefinition : createTable.getColumnDefinitions()) {
                Column column = new Column(createTable.getTable(), columnDefinition.getColumnName());
                ColDataType colDataType = columnDefinition.getColDataType();
                String columnName = column.getColumnName();
                String dataType = colDataType.getDataType();
                String comment = extractComment(columnDefinition);
//                columnInfoList.add(columnName, dataType);
            }

            return tableInfo;
        } catch (JSQLParserException e) {
            return null;
        }
    }

    public static String extractComment(ColumnDefinition columnDefinition) {
        List<String> specs = columnDefinition.getColumnSpecs();
        if (specs != null) {
            // 假设注释总是位于最后一个元素，且前一个元素是 'COMMENT' 关键字
            for (int i = 0; i < specs.size(); i++) {
                if ("COMMENT".equalsIgnoreCase(specs.get(i)) && i + 1 < specs.size()) {
                    // 返回注释
                    return specs.get(i + 1);
                }
            }
        }
        // 没有找到注释
        return null;
    }

    public static String extractTableComment(List<String> specs) {
        if (specs != null) {
            // 假设注释总是位于最后一个元素，且前一个元素是 'COMMENT' 关键字
            for (int i = 0; i < specs.size(); i++) {
                if ("COMMENT".equalsIgnoreCase(specs.get(i)) && i + 1 < specs.size()) {
                    String comment = specs.get(i + 1);
                    if ("=".equals(comment)) {
                        if (i + 2 < specs.size()) {
                            comment = specs.get(i + 2);
                        } else {
                            return null;
                        }
                    }
                    // 返回注释
                    return comment;
                }
            }
        }
        // 没有找到注释
        return null;
    }


    public static TableDetail parseCreateTableSql2(String sql) {
        TableDetail tableDetail = new TableDetail();

        // 去除多余空格，简化解析过程
        sql = sql.replaceAll("\\s+", " ").trim();

        // 假定SQL以 "CREATE TABLE" 开始
        if (!sql.toUpperCase().startsWith("CREATE TABLE")) {
            throw new IllegalArgumentException("不是有效的创建表语句");
        }

        // 提取表名
        String tableName = sql.split(" ")[2];
        tableDetail.setTableName(tableName);

        // 提取列定义
        String columnPart = sql.substring(sql.indexOf("(") + 1, sql.lastIndexOf(")"));
        String[] columns = columnPart.split(",");
        for (String column : columns) {
            column = column.trim();
            String[] parts = column.split(" ");
            String columnName = parts[0];
            String dataType = parts[1];
            tableDetail.addColumn(columnName, dataType);
        }

        return tableDetail;
    }

    public static void main(String[] args) throws JSQLParserException {
        String sql = "CREATE TABLE `artificial_session_record_log_index` (" +
                "  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键'," +
                "  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户id'," +
                "  `customer_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '客服userId 直聘账号ID'," +
                "  `group_type` tinyint(3) NOT NULL DEFAULT '0' COMMENT '用户属于技能组类别 1: 线下合作组 2: 大厂用户组 3:职务组 4:线上付费组 5:重复访问 6:种子用户组 7:认证组 8:客服组'," +
                "  `source` tinyint(2) NOT NULL DEFAULT '0' COMMENT '用户来源 1:转接接入  2:客服主动接通 3:系统自动分配'," +
                "  `platform` tinyint(3) NOT NULL DEFAULT '0' COMMENT '平台 1：安卓  2：ios 3: pc '," +
                "  `session_close_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '会话结束类型 1: 用户结束会话 2：客服结束会话  3:自动结束会话'," +
                "  `session_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '会话id，对应最开始的会话id'," +
                "  `message_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '消息id'," +
                "  `start_time` datetime DEFAULT NULL COMMENT '会话开始时间'," +
                "  `last_chat_time` datetime DEFAULT NULL COMMENT '最后一次用户说话时间'," +
                "  `add_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间'," +
                "  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                "  `navigation_config_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '导航问题ID'," +
                "  `exec_customer_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '执行客服userId 直聘账号ID'," +
                "  `last_reply_time` datetime DEFAULT NULL COMMENT '客服最后回复时间'," +
                "  `first_reply_time` datetime DEFAULT NULL COMMENT '客服首次回复时间'," +
                "  `sv_keys` tinyint(3) NOT NULL DEFAULT '-1' COMMENT '满意度按键'," +
                "  `evaluate_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '评价状态 问题处理状态 1：已解决  2：未解决'," +
                "  `wait_time` datetime DEFAULT NULL COMMENT '排队时间'," +
                "  `session_source` tinyint(3) NOT NULL DEFAULT '1' COMMENT '会话来源: 1 我的客服, 2 网络通话'," +
                "  `company_name` varchar(256) NOT NULL DEFAULT '' COMMENT '公司名称'," +
                "  `session_duration` int(8) NOT NULL DEFAULT '0' COMMENT '会话状态时长 单位毫秒'," +
                "  `fcr_solved_type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '首解率解决类型'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_userid` (`user_id`)," +
                "  KEY `idx_customer_user_id` (`customer_user_id`)," +
                "  KEY `idx_add_time` (`add_time`)," +
                "  KEY `idx_session_id` (`session_id`)," +
                "  KEY `idx_start_time` (`start_time`)," +
                "  KEY `idx_exec_customer_user_id` (`exec_customer_user_id`)," +
                "  KEY `idx_wait_time` (`wait_time`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人工会话记录日志索引表';";
        TableDetail tableDetail = SqlUtils.parseCreateTableSql1(SqlUtils.formatSql(sql));
        System.out.println("Table Name: " + tableDetail.getTableName());
        for (TableDetail.ColumnDetail column : tableDetail.getColumns()) {
            System.out.println("Column: " + column.getColumnName() + ", Type: " + column.getDataType());
        }
    }

}
