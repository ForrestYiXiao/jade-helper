package com.zhipin.jadehelper.tool;

import com.intellij.database.psi.DbTable;
import com.intellij.psi.PsiClass;
import lombok.Data;
import net.sf.jsqlparser.statement.Statement;

import java.util.List;

/**
 * 缓存数据工具类
 *
 * @author Zhang Yixiao
 * @version 1.0.0
 */
@Data
public class CacheDataUtils {
    private volatile static CacheDataUtils cacheDataUtils;

    /**
     * 单例模式
     */
    public static CacheDataUtils getInstance() {
        if (cacheDataUtils == null) {
            synchronized (CacheDataUtils.class) {
                if (cacheDataUtils == null) {
                    cacheDataUtils = new CacheDataUtils();
                }
            }
        }
        return cacheDataUtils;
    }

    private CacheDataUtils() {
    }

    /**
     * 输入内容
     */
    private Statement statement;

    /**
     * 当前选中的表
     */
    private DbTable selectDbTable;
    /**
     * 所有选中的表
     */
    private List<DbTable> dbTableList;

    /**
     * 选中的类
     */
    private PsiClass selectPsiClass;

    /**
     * 所有选中的表
     */
    private List<PsiClass> psiClassList;
}
