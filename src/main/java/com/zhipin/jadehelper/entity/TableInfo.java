package com.zhipin.jadehelper.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intellij.database.psi.DbTable;
import lombok.Data;
import net.sf.jsqlparser.statement.Statement;

import java.util.List;
import java.util.Vector;

/**
 * 表信息
 */
@Data
public class TableInfo {
    /**
     * 原始对象
     */
    @JsonIgnore
    private DbTable obj;

    /**
     * 原始对象（从实体生成）
     * Note: 实际类型是com.intellij.psi.PsiClass，为了避免velocity反射出现ClassNotFound，写为Object类型
     */
    @JsonIgnore
    private Object psiClassObj;

    /**
     * 用户输入的sql
     */
    @JsonIgnore
    private Statement statement;

    /**
     * 表名（首字母大写）
     */
    private String name;
    /**
     * 表名前缀
     */
    private String preName;
    /**
     * 注释
     */
    private String comment;
    /**
     * 模板组名称
     */
    private String templateGroupName;
    /**
     * 所有列
     */
    private List<ColumnInfo> fullColumn;
    /**
     * 主键列
     */
    private List<ColumnInfo> pkColumn;
    /**
     * 其他列
     */
    private List<ColumnInfo> otherColumn;
    /**
     * 保存的包名称
     */
    private String savePackageName;
    /**
     * 保存路径
     */
    private String savePath;
    /**
     * 保存的model名称
     */
    private String saveModelName;

    private Vector<Vector> dataVector;
}
