package com.zhipin.jadehelper.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 生成选项
 */
@Data
@Builder
public class GenerateOptions {
    /**
     * 实体类模式
     */
    private Integer entityModel;
    /**
     * 统一配置
     */
    private Boolean unifiedConfig;
    /**
     * 重新格式化代码
     */
    private Boolean reFormat;
    /**
     * 提示选是
     */
    private Boolean titleSure;
    /**
     * 提示选否
     */
    private Boolean titleRefuse;
}
