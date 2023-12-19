package com.zhipin.jadehelper.ui;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ExceptionUtil;
import com.zhipin.jadehelper.constants.MainEntryConstants;
import com.zhipin.jadehelper.constants.StrState;
import com.zhipin.jadehelper.dict.GlobalDict;
import com.zhipin.jadehelper.dto.GenerateOptions;
import com.zhipin.jadehelper.dto.SettingsStorageDTO;
import com.zhipin.jadehelper.entity.TableInfo;
import com.zhipin.jadehelper.entity.Template;
import com.zhipin.jadehelper.service.CodeGenerateService;
import com.zhipin.jadehelper.service.SettingsStorageService;
import com.zhipin.jadehelper.service.TableInfoSettingsService;
import com.zhipin.jadehelper.tool.CacheDataUtils;
import com.zhipin.jadehelper.tool.ModuleUtils;
import com.zhipin.jadehelper.tool.ProjectUtils;
import com.zhipin.jadehelper.tool.StringUtils;
//import com.zhipin.jadehelper.ui.base.JTableCustomRenderer;
//import com.zhipin.jadehelper.ui.base.JTableCustomerEditor;
//import com.zhipin.jadehelper.ui.base.JTableModulePathModel;
import com.zhipin.jadehelper.ui.base.jtable.t2.MyTable;
import com.zhipin.jadehelper.ui.base.jtable.t2.MyTableModel;
import com.zhipin.jadehelper.ui.component.TemplateSelectComponent;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * 选择保存路径
 */
public class SelectSavePath extends DialogWrapper {
    /**
     * 主面板
     */
    private JPanel contentPane;
    /**
     * 包字段
     */
    private JTextField packageField;
    /**
     * 路径字段
     */
    private JTextField pathField;
    /**
     * 包选择按钮
     */
    private JButton packageChooseButton;
    /**
     * 路径选择按钮
     */
    private JButton pathChooseButton;
    /**
     * 模板面板
     */
    private JPanel templatePanel;
    /**
     * 格式化代码复选框
     */
    private JCheckBox reFormatCheckBox;

    private JTable generateListTable;

    /**
     * 动态生成的创建代码列面板
     */
    private JScrollPane dynamicGeneratePane;
    private JPanel generateCodePanel;
    /**
     * 数据缓存工具类
     */
    private CacheDataUtils cacheDataUtils = CacheDataUtils.getInstance();
    /**
     * 表信息服务
     */
    private TableInfoSettingsService tableInfoService;
    /**
     * 项目对象
     */
    private Project project;
    /**
     * 代码生成服务
     */
    private CodeGenerateService codeGenerateService;
    /**
     * 当前项目中的module
     */
    private List<Module> moduleList;

    /**
     * 实体模式生成代码
     * 1:selectPsiClass
     * 2:selectDbTable
     * 3:inputSQL
     */
    private int entityMode;

    /**
     * 模板选择组件
     */
    private TemplateSelectComponent templateSelectComponent;

    /**
     * 构造方法
     */
    public SelectSavePath(Project project) {
        this(project, MainEntryConstants.SELECT_PATH_DB_TABLE);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return this.contentPane;
    }

    /**
     * 构造方法
     */
    public SelectSavePath(Project project, int entityMode) {
        super(project);
        this.entityMode = entityMode;
        this.project = project;
        this.tableInfoService = TableInfoSettingsService.getInstance();
        this.codeGenerateService = CodeGenerateService.getInstance(project);
        // 初始化module，存在资源路径的排前面
        this.moduleList = new LinkedList<>();
        for (Module module : ModuleManager.getInstance(project).getModules()) {
            // 存在源代码文件夹放前面，否则放后面
            if (ModuleUtils.existsSourcePath(module)) {
                this.moduleList.add(0, module);
            } else {
                this.moduleList.add(module);
            }
        }

        this.initPanel();
        this.refreshData();
        this.initEvent();
        init();
        setTitle(GlobalDict.TITLE_INFO);
        //初始化路径
        refreshPath();

    }


    private void initEvent() {

        try {
            Class<?> cls = Class.forName("com.intellij.ide.util.PackageChooserDialog");
            //添加包选择事件
            packageChooseButton.addActionListener(e -> {
                try {
                    Constructor<?> constructor = cls.getConstructor(String.class, Project.class);
                    Object dialog = constructor.newInstance("Package Chooser", project);
                    // 显示窗口
                    Method showMethod = cls.getMethod("show");
                    showMethod.invoke(dialog);
                    // 获取选中的包名
                    Method getSelectedPackageMethod = cls.getMethod("getSelectedPackage");
                    Object psiPackage = getSelectedPackageMethod.invoke(dialog);
                    if (psiPackage != null) {
                        Method getQualifiedNameMethod = psiPackage.getClass().getMethod("getQualifiedName");
                        String packageName = (String) getQualifiedNameMethod.invoke(psiPackage);
                        packageField.setText(packageName);
                        // 刷新路径
                        refreshPath();
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                         InvocationTargetException e1) {
                    ExceptionUtil.rethrow(e1);
                }
            });

            // 添加包编辑框失去焦点事件
            packageField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    // 刷新路径
                    refreshPath();
                }
            });
        } catch (ClassNotFoundException e) {
            // 没有PackageChooserDialog，并非支持Java的IDE，禁用相关UI组件
            packageField.setEnabled(false);
            packageChooseButton.setEnabled(false);
        }

        //选择路径
        pathChooseButton.addActionListener(e -> {
            //将当前选中的model设置为基础路径
            VirtualFile path = ProjectUtils.getBaseDir(project);
            Module module = getSelectModule();
            if (module != null) {
                path = ModuleUtils.getSourcePath(module);
            }
            VirtualFile virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFolderDescriptor(), project, path);
            if (virtualFile != null) {
                pathField.setText(virtualFile.getPath());
            }
        });
    }

    private void refreshData() {
        // 获取选中的表信息（鼠标右键的那张表），并提示未知类型
        TableInfo tableInfo;
        if (MainEntryConstants.SELECT_PATH_PSI_CLASS == entityMode) {
            tableInfo = tableInfoService.getTableInfo(cacheDataUtils.getSelectPsiClass());
        } else if (MainEntryConstants.SELECT_PATH_DB_TABLE == entityMode) {
            tableInfo = tableInfoService.getTableInfo(cacheDataUtils.getSelectDbTable());
        } else {
            tableInfo = tableInfoService.getTableInfo(cacheDataUtils.getStatement());
        }
        if (!StringUtils.isEmpty(tableInfo.getSavePackageName())) {
            packageField.setText(tableInfo.getSavePackageName());
        }
        SettingsStorageDTO settings = SettingsStorageService.getSettingsStorage();
        String groupName = settings.getCurrTemplateGroupName();
        if (!StringUtils.isEmpty(tableInfo.getTemplateGroupName())) {
            if (settings.getTemplateGroupMap().containsKey(tableInfo.getTemplateGroupName())) {
                groupName = tableInfo.getTemplateGroupName();
            }
        }
        templateSelectComponent.setSelectedGroupName(groupName);
        String savePath = tableInfo.getSavePath();
        if (!StringUtils.isEmpty(savePath)) {
            // 判断是否需要拼接项目路径
            if (savePath.startsWith(StrState.RELATIVE_PATH)) {
                String projectPath = project.getBasePath();
                savePath = projectPath + savePath.substring(1);
            }
            pathField.setText(savePath);
        }
    }

    @Override
    protected void doOKAction() {
        onOK();
        super.doOKAction();
        JOptionPane.showMessageDialog(null, "代码初始化成功!");
    }

    /**
     * 确认按钮回调事件
     */
    private void onOK() {
        List<Template> selectTemplateList = templateSelectComponent.getAllSelectedTemplate();
        // 如果选择的模板是空的
        if (selectTemplateList.isEmpty()) {
            Messages.showWarningDialog("Can't Select Template!", GlobalDict.TITLE_INFO);
            return;
        }
        String savePath = pathField.getText();
        if (StringUtils.isEmpty(savePath)) {
            Messages.showWarningDialog("Can't Select Save Path!", GlobalDict.TITLE_INFO);
            return;
        }
        // 针对Linux系统路径做处理
        savePath = savePath.replace("\\", "/");
        // 保存路径使用相对路径
        String basePath = project.getBasePath();
        if (!StringUtils.isEmpty(basePath) && savePath.startsWith(basePath)) {
            if (savePath.length() > basePath.length()) {
                if ("/".equals(savePath.substring(basePath.length(), basePath.length() + 1))) {
                    savePath = savePath.replace(basePath, ".");
                }
            } else {
                savePath = savePath.replace(basePath, ".");
            }
        }
        // 保存配置
        TableInfo tableInfo;
        if (MainEntryConstants.SELECT_PATH_DB_TABLE == entityMode) {
            tableInfo = tableInfoService.getTableInfo(cacheDataUtils.getSelectDbTable());
        } else if (MainEntryConstants.SELECT_PATH_PSI_CLASS == entityMode) {
            tableInfo = tableInfoService.getTableInfo(cacheDataUtils.getSelectPsiClass());
        } else {
            tableInfo = tableInfoService.getTableInfo(cacheDataUtils.getStatement());
        }
        tableInfo.setSavePath(savePath);
        tableInfo.setSavePackageName(packageField.getText());
        tableInfo.setTemplateGroupName(templateSelectComponent.getSelectedGroupName());
        tableInfo.setDataVector(templateSelectComponent.getModel().getDataVector());
        Module module = getSelectModule();
        if (module != null) {
            tableInfo.setSaveModelName(module.getName());
        }
        // 保存配置
        tableInfoService.saveTableInfo(tableInfo);

        codeGenerateService.generate(selectTemplateList, getGenerateOptions());
    }

    /**
     * 初始化方法
     */
    private void initPanel() {

//        JTableModulePathModel model = new JTableModulePathModel();
//        generateList.setModel(model);
//        // 设置自定义渲染器和编辑器
//        JTableCustomerEditor customEditor = new JTableCustomerEditor(this.moduleList);
//        generateList.setDefaultRenderer(Object.class, new JTableCustomRenderer(customEditor));
//        generateList.setDefaultEditor(Object.class, customEditor);
//        generateList.setSurrendersFocusOnKeystroke(true);
        MyTableModel model = new MyTableModel();
//        generateListTable.init(new MyTableModel(), this.moduleList);
        generateListTable.setModel(model);
        // 创建下拉框并填充模块列表
        JComboBox<Module> comboBox = new JComboBox<>();
        if (CollectionUtils.isNotEmpty(moduleList)) {
            moduleList.forEach(comboBox::addItem);
        }

        TableColumn tempColumn = generateListTable.getColumnModel().getColumn(0);
        tempColumn.setCellEditor(new DefaultCellEditor(new JTextField()));

        // 设置下拉框编辑器到第二列
        TableColumn moduleColumn = generateListTable.getColumnModel().getColumn(1);
        moduleColumn.setCellEditor(new DefaultCellEditor(comboBox));

        // 设置文本编辑器到第三列
        TableColumn textColumn = generateListTable.getColumnModel().getColumn(2);
        textColumn.setCellEditor(new DefaultCellEditor(new JTextField()));


        generateListTable.setRowHeight(30);
//        model.fireTableDataChanged(); // 通知数据变化
//        generateListTable.revalidate();
//        generateListTable.repaint();
        // 初始化模板组
        this.templateSelectComponent = new TemplateSelectComponent(model, this.moduleList, packageField);
//        this.generateListComponent = new GenerateListComponent(this.templateSelectComponent.getGroupComboBox());
        templatePanel.add(this.templateSelectComponent.getMainPanel(), BorderLayout.CENTER);
    }

    /**
     * 获取生成选项
     *
     * @return {@link GenerateOptions}
     */
    private GenerateOptions getGenerateOptions() {
        return GenerateOptions.builder()
                .entityModel(this.entityMode)
                .reFormat(reFormatCheckBox.isSelected())
//                .titleSure(titleSureCheckBox.isSelected())
//                .titleRefuse(titleRefuseCheckBox.isSelected())
//                .unifiedConfig(unifiedConfigCheckBox.isSelected())
                .build();
    }

    /**
     * 获取选中的Module
     *
     * @return 选中的Module
     */
    private Module getSelectModule() {
        String name = findTopLevelModulePath(project);
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return ModuleManager.getInstance(project).findModuleByName(name);
    }


    public String findTopLevelModulePath(Project project) {
        ModuleManager moduleManager = ModuleManager.getInstance(project);
        Module[] modules = moduleManager.getModules();

        Module topLevelModule = findTopLevelModule(modules);

        if (topLevelModule != null) {
            return topLevelModule.getName();
        }

        return null;
    }

    private Module findTopLevelModule(Module[] modules) {
        Module topLevelModule = null;
        int shortestPathLength = Integer.MAX_VALUE;

        for (Module module : modules) {
            String modulePath = module.getModuleFilePath();
            int pathLength = Paths.get(modulePath).getNameCount();

            if (pathLength < shortestPathLength) {
                shortestPathLength = pathLength;
                topLevelModule = module;
            }
        }

        return topLevelModule;
    }

    /**
     * 获取基本路径
     *
     * @return 基本路径
     */
    private String getBasePath() {
        Module module = getSelectModule();
        VirtualFile baseVirtualFile = ProjectUtils.getBaseDir(project);
        if (baseVirtualFile == null) {
            Messages.showWarningDialog("无法获取到项目基本路径！", GlobalDict.TITLE_INFO);
            return "";
        }
        String baseDir = baseVirtualFile.getPath();
        if (module != null) {
            VirtualFile virtualFile = ModuleUtils.getSourcePath(module);
            if (virtualFile != null) {
                baseDir = virtualFile.getPath();
            }
        }
        return baseDir;
    }

    /**
     * 刷新目录
     */
    private void refreshPath() {
        String packageName = packageField.getText();
//        // 获取基本路径
        String path = getBasePath();
        // 兼容Linux路径
        path = path.replace("\\", "/");
        // 如果存在包路径，添加包路径
//        if (!StringUtils.isEmpty(packageName)) {
//            path += "/" + packageName.replace(".", "/");
//        }
        pathField.setText(path);
    }

    public void createUIComponents() {

    }
}
