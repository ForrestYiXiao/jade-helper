package com.zhipin.jadehelper.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.intellij.database.util.DasUtil;
import com.intellij.database.util.DbUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.util.ReflectionUtil;
import com.zhipin.jadehelper.constants.MainEntryConstants;
import com.zhipin.jadehelper.dict.GlobalDict;
import com.zhipin.jadehelper.dto.GenerateOptions;
import com.zhipin.jadehelper.dto.SettingsStorageDTO;
import com.zhipin.jadehelper.entity.Callback;
import com.zhipin.jadehelper.entity.SaveFile;
import com.zhipin.jadehelper.entity.TableInfo;
import com.zhipin.jadehelper.entity.Template;
import com.zhipin.jadehelper.service.CodeGenerateService;
import com.zhipin.jadehelper.service.SettingsStorageService;
import com.zhipin.jadehelper.service.TableInfoSettingsService;
import com.zhipin.jadehelper.tool.*;
import net.sf.jsqlparser.statement.Statement;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zhang Yixiao
 * @version 1.0.0
 */
public class CodeGenerateServiceImpl implements CodeGenerateService {
    /**
     * 项目对象
     */
    private Project project;
    /**
     * 模型管理
     */
    private ModuleManager moduleManager;
    /**
     * 表信息服务
     */
    private TableInfoSettingsService tableInfoService;
    /**
     * 缓存数据工具
     */
    private CacheDataUtils cacheDataUtils;
    /**
     * 导入包时过滤的包前缀
     */
    private static final String FILTER_PACKAGE_NAME = "java.lang";

    public CodeGenerateServiceImpl(Project project) {
        this.project = project;
        this.moduleManager = ModuleManager.getInstance(project);
        this.tableInfoService = TableInfoSettingsService.getInstance();
        this.cacheDataUtils = CacheDataUtils.getInstance();
    }

    /**
     * 生成
     *
     * @param templates       模板
     * @param generateOptions 生成选项
     */
    @Override
    public void generate(Collection<Template> templates, GenerateOptions generateOptions) {
        // 获取选中表信息
        TableInfo selectedTableInfo = null;
        List<TableInfo> tableInfoList = null;
        if (MainEntryConstants.SELECT_PATH_DB_TABLE == generateOptions.getEntityModel()) {
            selectedTableInfo = tableInfoService.getTableInfo(cacheDataUtils.getSelectPsiClass());
            tableInfoList = cacheDataUtils.getPsiClassList().stream().map(item -> tableInfoService.getTableInfo(item)).collect(Collectors.toList());
        } else if (MainEntryConstants.SELECT_PATH_PSI_CLASS == generateOptions.getEntityModel()) {
            selectedTableInfo = tableInfoService.getTableInfo(cacheDataUtils.getSelectDbTable());
            tableInfoList = cacheDataUtils.getDbTableList().stream().map(item -> tableInfoService.getTableInfo(item)).collect(Collectors.toList());
        } else {
            selectedTableInfo = tableInfoService.getTableInfo(cacheDataUtils.getStatement());
            tableInfoList = Lists.newArrayList(selectedTableInfo);
        }
        // 校验选中表的保存路径是否正确
        // 暂时放弃校验
//        if (StringUtils.isEmpty(selectedTableInfo.getSavePath())) {
//            if (selectedTableInfo.getObj() != null) {
//                Messages.showInfoMessage(selectedTableInfo.getObj().getName() + "表配置信息不正确，请尝试重新配置", GlobalDict.TITLE_INFO);
//            } else if (selectedTableInfo.getPsiClassObj() != null) {
//                PsiClass psiClassObj = (PsiClass) selectedTableInfo.getPsiClassObj();
//                Messages.showInfoMessage(psiClassObj.getName() + "类配置信息不正确，请尝试重新配置", GlobalDict.TITLE_INFO);
//            } else if (selectedTableInfo.getStatement() != null) {
//                Messages.showInfoMessage("SQL输入有问题，请尝试修改后重新输入", GlobalDict.TITLE_INFO);
//            } else {
//                Messages.showInfoMessage("配置信息不正确，请尝试重新配置", GlobalDict.TITLE_INFO);
//            }
//            return;
//        }
        // 将未配置的表进行配置覆盖
        TableInfo finalSelectedTableInfo = selectedTableInfo;
        tableInfoList.forEach(tableInfo -> {
            if (StringUtils.isEmpty(tableInfo.getSavePath())) {
                tableInfo.setSaveModelName(finalSelectedTableInfo.getSaveModelName());
                tableInfo.setSavePackageName(finalSelectedTableInfo.getSavePackageName());
                tableInfo.setSavePath(finalSelectedTableInfo.getSavePath());
                tableInfo.setPreName(finalSelectedTableInfo.getPreName());
                tableInfoService.saveTableInfo(tableInfo);
            }
        });
        // 如果使用统一配置，直接全部覆盖
        if (Boolean.TRUE.equals(generateOptions.getUnifiedConfig())) {
            tableInfoList.forEach(tableInfo -> {
                tableInfo.setSaveModelName(finalSelectedTableInfo.getSaveModelName());
                tableInfo.setSavePackageName(finalSelectedTableInfo.getSavePackageName());
                tableInfo.setSavePath(finalSelectedTableInfo.getSavePath());
                tableInfo.setPreName(finalSelectedTableInfo.getPreName());
            });
        }

        // 生成代码
        generate(templates, tableInfoList, generateOptions, null);
    }

    /**
     * 生成代码，并自动保存到对应位置
     *
     * @param templates       模板
     * @param tableInfoList   表信息对象
     * @param generateOptions 生成配置
     * @param otherParam      其他参数
     */
    public void generate(Collection<Template> templates, Collection<TableInfo> tableInfoList, GenerateOptions generateOptions, Map<String, Object> otherParam) {
        if (CollectionUtil.isEmpty(templates) || CollectionUtil.isEmpty(tableInfoList)) {
            return;
        }
        // 处理模板，注入全局变量（克隆一份，防止篡改）
        templates = CloneUtils.cloneByJson(templates, new TypeReference<ArrayList<Template>>() {
        });
        TemplateUtils.addGlobalConfig(templates);
        // 生成代码
        for (TableInfo tableInfo : tableInfoList) {
            // 表名去除前缀
            if (!StringUtils.isEmpty(tableInfo.getPreName()) && tableInfo.getObj().getName().startsWith(tableInfo.getPreName())) {
                String newName = tableInfo.getObj().getName().substring(tableInfo.getPreName().length());
                tableInfo.setName(NameUtils.getInstance().getClassName(newName));
            }
            // 构建参数
            Map<String, Object> param = getDefaultParam();
            // 其他参数
            if (otherParam != null) {
                param.putAll(otherParam);
            }
            // 所有表信息对象
            param.put("tableInfoList", tableInfoList);
            // 表信息对象
            param.put("tableInfo", tableInfo);
            // 设置模型路径与导包列表
            setModulePathAndImportList(param, tableInfo);
            // 设置额外代码生成服务
            param.put("generateService", new ExtraCodeGenerateUtils(this, tableInfo, generateOptions));
            for (Template template : templates) {
                // 根据template 获取勾选的module和path
                tableInfo.getDataVector().forEach(item -> {
                    if (item.get(0).equals(template.getName())) {
                        Module module = (Module) item.get(1);
                        tableInfo.setSaveModelName(module.getName());
                        String modulePath = this.moduleManager.findModuleByName(module.getName()).getModuleFile().getParent().getPath();
                        tableInfo.setSavePath(modulePath + "/" + item.get(2));
                    }
                });
                Callback callback = new Callback();
                callback.setWriteFile(true);
                callback.setReformat(generateOptions.getReFormat());
                // 默认名称
                callback.setFileName(tableInfo.getName() + "Default.java");
                // 默认路径
                callback.setSavePath(tableInfo.getSavePath());
                // 设置回调对象
                param.put("callback", callback);
                // 开始生成
                String code = VelocityUtils.generate(template.getCode(), param);
                // 设置一个默认保存路径与默认文件名
                String path = callback.getSavePath();
                path = path.replace("\\", "/");
                // 针对相对路径进行处理
                if (path.startsWith(".")) {
                    path = project.getBasePath() + path.substring(1);
                }
                callback.setSavePath(path);
                new SaveFile(project, code, callback, generateOptions).write();
            }
        }
    }

    /**
     * 生成代码
     *
     * @param template  模板
     * @param tableInfo 表信息对象
     * @return 生成好的代码
     */
    @Override
    public String generate(Template template, TableInfo tableInfo) {
        // 获取默认参数
        Map<String, Object> param = getDefaultParam();
        // 表信息对象，进行克隆，防止篡改
        param.put("tableInfo", tableInfo);
        // 设置模型路径与导包列表
        setModulePathAndImportList(param, tableInfo);
        // 处理模板，注入全局变量
        TemplateUtils.addGlobalConfig(template);
        return VelocityUtils.generate(template.getCode(), param);
    }

    /**
     * 设置模型路径与导包列表
     *
     * @param param     参数
     * @param tableInfo 表信息对象
     */
    private void setModulePathAndImportList(Map<String, Object> param, TableInfo tableInfo) {
        Module module = null;
        if (!StringUtils.isEmpty(tableInfo.getSaveModelName())) {
            module = this.moduleManager.findModuleByName(tableInfo.getSaveModelName());
        }
        if (module != null) {
            // 设置modulePath
            param.put("modulePath", ModuleUtils.getModuleDir(module).getPath());
        }
        // 设置要导入的包
        param.put("importList", getImportList(tableInfo));
    }

    /**
     * 获取默认参数
     *
     * @return 参数
     */
    private Map<String, Object> getDefaultParam() {
        // 系统设置
        SettingsStorageDTO settings = SettingsStorageService.getSettingsStorage();
        Map<String, Object> param = new HashMap<>(20);
        // 作者
        param.put("author", settings.getAuthor());
        //工具类
        param.put("tool", GlobalTool.getInstance());
        param.put("time", TimeUtils.getInstance());
        // 项目路径
        param.put("projectPath", project.getBasePath());
        // Database数据库工具
        param.put("dbUtil", ReflectionUtil.newInstance(DbUtil.class));
        param.put("dasUtil", ReflectionUtil.newInstance(DasUtil.class));
        return param;
    }

    /**
     * 获取导入列表
     *
     * @param tableInfo 表信息对象
     * @return 导入列表
     */
    private Set<String> getImportList(TableInfo tableInfo) {
        // 创建一个自带排序的集合
        Set<String> result = new TreeSet<>();
        tableInfo.getFullColumn().forEach(columnInfo -> {
            if (!columnInfo.getType().startsWith(FILTER_PACKAGE_NAME)) {
                String type = NameUtils.getInstance().getClsFullNameRemoveGeneric(columnInfo.getType());
                result.add(type);
            }
        });
        return result;
    }
}
