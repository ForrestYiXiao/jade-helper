package com.zhipin.jadehelper.tool;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Zhang Yixiao
 * @date 2023/10/29 17:23
 */
public class TableDetail {
    private String tableName;
    private List<ColumnDetail> columns;

    public TableDetail() {
        columns = new ArrayList<>();
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void addColumn(String columnName, String dataType) {
        columns.add(new ColumnDetail(columnName, dataType));
    }

    public String getTableName() {
        return tableName;
    }

    public List<ColumnDetail> getColumns() {
        return columns;
    }

    public static class ColumnDetail {
        private String columnName;
        private String dataType;

        public ColumnDetail(String columnName, String dataType) {
            this.columnName = columnName;
            this.dataType = dataType;
        }

        public String getColumnName() {
            return columnName;
        }

        public String getDataType() {
            return dataType;
        }
    }
}
