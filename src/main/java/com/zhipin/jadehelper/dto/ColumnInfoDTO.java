package com.zhipin.jadehelper.dto;

import com.intellij.database.model.DasColumn;
import com.intellij.psi.PsiField;
import com.zhipin.jadehelper.entity.TypeMapper;
import com.zhipin.jadehelper.enums.MatchType;
import com.zhipin.jadehelper.tool.CurrGroupUtils;
import com.zhipin.jadehelper.tool.DocCommentUtils;
import com.zhipin.jadehelper.tool.NameUtils;
import com.zhipin.jadehelper.tool.SqlUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;

import java.util.regex.Pattern;

/**
 * 列信息传输对象
 */
@Data
@NoArgsConstructor
public class ColumnInfoDTO {

    public ColumnInfoDTO(PsiField field) {
        this.name = field.getName();
        this.columnName = NameUtils.getInstance().hump2Underline(field.getName());
        this.comment = DocCommentUtils.getComment(field.getDocComment());
        this.type = field.getType().getCanonicalText();
        this.custom = false;
        this.ext = "{}";
    }

    public ColumnInfoDTO(DasColumn column) {
        this.name = NameUtils.getInstance().getJavaName(column.getName());
        this.columnName = column.getName();
        this.comment = column.getComment();
        this.type = getJavaType(column.getDataType().toString());
        this.custom = false;
        this.ext = "{}";
    }

    public ColumnInfoDTO(ColumnDefinition column) {
        this.name = NameUtils.getInstance().getJavaName(column.getColumnName());
        this.columnName = column.getColumnName();
        this.comment = SqlUtils.extractComment(column);
        this.type = getJavaType(column.getColDataType().getDataType());
        this.custom = false;
        this.ext = "{}";
    }

    private String getJavaType(String dbType) {
        for (TypeMapper typeMapper : CurrGroupUtils.getCurrTypeMapperGroup().getElementList()) {
            if (typeMapper.getMatchType() == MatchType.ORDINARY) {
                if (dbType.equalsIgnoreCase(typeMapper.getColumnType())) {
                    return typeMapper.getJavaType();
                }
            } else {
                // 不区分大小写的正则匹配模式
                if (Pattern.compile(typeMapper.getColumnType(), Pattern.CASE_INSENSITIVE).matcher(dbType).matches()) {
                    return typeMapper.getJavaType();
                }
            }
        }
        return "java.lang.Object";
    }

    /**
     * 名称
     */
    private String name;

    /**
     * 名称
     */
    private String columnName;
    /**
     * 注释
     */
    private String comment;
    /**
     * 全类型
     */
    private String type;
    /**
     * 标记是否为自定义附加列
     */
    private Boolean custom;
    /**
     * 扩展数据(JSON字符串)
     */
    private String ext;
}
