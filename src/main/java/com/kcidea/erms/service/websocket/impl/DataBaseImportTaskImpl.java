package com.kcidea.erms.service.websocket.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.util.*;
import com.kcidea.erms.dao.database.DatabaseLevelDao;
import com.kcidea.erms.dao.ers.SchoolDatabaseAliasDao;
import com.kcidea.erms.dao.ers.SchoolDatabaseListDao;
import com.kcidea.erms.dao.ers.VdatabaseDao;
import com.kcidea.erms.dao.ers.VdatabasePropertyDao;
import com.kcidea.erms.dao.task.ErmsTaskDao;
import com.kcidea.erms.dao.task.ErmsTaskRecordDao;
import com.kcidea.erms.domain.database.DatabaseLevel;
import com.kcidea.erms.domain.task.ErmsTask;
import com.kcidea.erms.domain.task.ErmsTaskRecord;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.common.EnumWebSocketApi;
import com.kcidea.erms.enums.database.EnumDataBaseType;
import com.kcidea.erms.enums.database.EnumLanguageType;
import com.kcidea.erms.enums.database.EnumPaperType;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.task.EnumExcelHeadlineType;
import com.kcidea.erms.enums.task.EnumTaskResult;
import com.kcidea.erms.enums.task.EnumTaskState;
import com.kcidea.erms.enums.task.EnumTaskType;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.common.WebSocketTaskModel;
import com.kcidea.erms.model.database.detail.DatabaseInfoModel;
import com.kcidea.erms.model.database.info.DatabaseInfoInsertModel;
import com.kcidea.erms.model.menu.MenuModel;
import com.kcidea.erms.model.task.DatabaseInfoExcelModel;
import com.kcidea.erms.model.task.TaskResultModel;
import com.kcidea.erms.model.user.UserModel;
import com.kcidea.erms.service.database.DataBaseInfoService;
import com.kcidea.erms.service.ers.DatabaseService;
import com.kcidea.erms.service.menu.MenuService;
import com.kcidea.erms.service.task.TaskService;
import com.kcidea.erms.service.websocket.WebSocketTaskBaseService;
import com.kcidea.erms.service.websocket.WebSocketTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/25
 **/
@Slf4j
@Service
public class DataBaseImportTaskImpl extends WebSocketTaskBaseService implements WebSocketTaskService {

    @Value("${my-config.temp-path}")
    private String TEMP_PATH;

    @Resource
    private ErmsTaskDao taskDao;

    @Resource
    private ErmsTaskRecordDao taskRecordDao;

    @Resource
    private VdatabaseDao vdatabaseDao;

    @Resource
    private VdatabasePropertyDao vdatabasePropertyDao;

    @Resource
    private SchoolDatabaseListDao schoolDatabaseListDao;

    @Resource
    private MenuService menuService;

    @Resource
    private SchoolDatabaseAliasDao schoolDatabaseAliasDao;

    @Resource
    private DatabaseService databaseService;

    @Resource
    private DatabaseLevelDao databaseLevelDao;

    @Resource
    private DataBaseInfoService dataBaseInfoService;

    @Resource
    private TaskService taskService;

    /**
     * 获取WebSocketApi工厂
     *
     * @return WebSocketApi工厂
     * @author huxubin
     * @date 2021/11/25 16:11
     */
    @Override
    public EnumWebSocketApi getWebSocketApi() {
        return EnumWebSocketApi.数据库信息列表导入;
    }

    /**
     * 校验权限
     *
     * @param uuid      操作人uuid
     * @param userModel 请求用户
     * @author huxubin
     * @date 2021/11/25 15:52
     */
    @Override
    public void checkAdminMenu(String uuid, UserModel userModel) {
        //从redis中获取用户的全部权限
        List<MenuModel> menuList = menuService.findMenuListByRoleId(userModel.getRoleId());
        if (!RightsUtil.checkAdminMenu(menuList, EnumMenu.数据库列表.getName(), EnumUserAction.导入)) {
            String errMsg = MessageFormat.format("很抱歉，您没有{0}权限，无法进行此操作",
                    EnumUserAction.导入.getName());
            // 没有权限，发送执行失败的消息
            super.sendWebSocketProgressMsg(uuid, null, null, null, errMsg);
            throw new CustomException(errMsg);
        }
    }

    /**
     * 执行WebSocket任务
     *
     * @param uuid      操作人uuid
     * @param userModel 请求用户
     * @param jsonData  请求的数据
     * @author huxubin
     * @date 2021/11/25 15:52
     */
    @Override
    public void executeTask(String uuid, UserModel userModel, Object jsonData) {
        // 解析jsonData
        WebSocketTaskModel webSocketTaskModel = JSONObject.toJavaObject((JSON) jsonData, WebSocketTaskModel.class);
        String fileName = webSocketTaskModel.getFileName();
        String filePath = webSocketTaskModel.getFilePath();
        Integer coverFlag = webSocketTaskModel.getCoverFlag();

        if (Strings.isNullOrEmpty(fileName) || Strings.isNullOrEmpty(filePath) || null == coverFlag) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 任务名称
        String taskName = DateTimeUtil.localDateTimeToString(LocalDateTime.now(), Constant.Pattern.DATE_NAME)
                .concat(Constant.SplitChar.LINE_CHAR).concat(EnumTaskType.数据库信息列表导入.getName());

        ErmsTask task = taskService.addTask(userModel.getSid(), taskName, EnumTaskType.数据库信息列表导入.getValue(),
                EnumTaskState.正在执行.getValue(), JSON.toJSONString(taskName), fileName, filePath, userModel.getId(),
                Boolean.FALSE);
        ErmsTaskRecord taskRecord = new ErmsTaskRecord().setTaskId(task.getId()).setStartTime(LocalDateTime.now());
        try {
            TaskResultModel taskResult =
                    this.executeImportTask(coverFlag, task, userModel.getSid(), userModel.getId(), uuid);

            //更新任务状态
            task.updateByTaskResult(taskResult);

            //更新任务结果日志
            taskRecord.setTaskLog(taskResult.getTaskLog());
        } catch (Exception ex) {
            log.error("执行数据库信息导入任务失败，原因：" + ex.getMessage());
            //更新任务状态
            task.setTaskState(EnumTaskState.执行失败.getValue());
            //更新任务结果
            taskRecord.setTaskLog(Vm.TASK_ERROR.concat(ex.getMessage()));

            //发送执行失败的消息
            super.sendWebSocketProgressMsg(uuid, null, null, null,
                    "执行数据库信息导入任务失败。");
        } finally {
            //任务结果保存
            taskRecord.setEndTime(LocalDateTime.now());
            taskRecord.setCreatedBy(userModel.getId());
            taskRecord.setCreatedTime(LocalDateTime.now());
            taskRecordDao.insert(taskRecord);

            //更新任务
            taskDao.updateById(task);
        }

    }

    /**
     * 执行导入任务
     *
     * @param coverFlag 覆盖标识
     * @param task      任务
     * @param sid       学校id
     * @param userId    操作人id
     * @param uuid      操作人uuid
     * @return 执行结果
     * @author yeweiwei
     * @date 2021/11/29 14:53
     */
    private TaskResultModel executeImportTask(Integer coverFlag, ErmsTask task, Long sid, Long userId,
                                              String uuid) throws Exception {
        //读取文件
        List<DatabaseInfoExcelModel> excelList = this.readExcelData(task.getFilePath());
        //合计数量
        int total = excelList.size();
        //任务执行进度
        int number = 1;

        int successCount = 0;
        int errorCount = 0;
        int warningCount = 0;

        //准备数据
        System.out.println("准备基础数据");
        long t1 = System.currentTimeMillis();
        //数据库id名称集合
        List<IdNameModel> databaseList = vdatabaseDao.findIdNameListBySid(sid);
        Map<String, Set<Long>> dbNameIdSetMap = databaseList.stream()
                .collect(Collectors.groupingBy(IdNameModel::getName,
                        Collectors.mapping(IdNameModel::getId, Collectors.toSet())));
        Set<Long> orderDidSet = schoolDatabaseListDao.findOrderDidSetBySidYear(sid, null);

        //查询数据库属性
        Map<String, Long> propertyNameIdMap = vdatabasePropertyDao.findListBySid(0L).stream()
                .collect(Collectors.toMap(IdNameModel::getName, IdNameModel::getId));

        //数据库id别名集合
        Map<String, Long> aliasDidMap = getNameIdMap(schoolDatabaseAliasDao.findIdNameListBySid(sid));

        System.out.println("基础数据准备完成，耗时：" + (System.currentTimeMillis() - t1));

        // 发送开始执行任务的消息
        String msg = MessageFormat.format("即将开始执行数据库信息导入任务，共{0}条数据。", total);
        super.sendWebSocketProgressMsg(uuid, total, 0, successCount, msg);

        //处理文件数据
        for (DatabaseInfoExcelModel excelModel : excelList) {
            try {
                EnumTaskResult result = this.saveDataBaseInfoData(excelModel, sid, userId, dbNameIdSetMap,
                        orderDidSet, aliasDidMap, propertyNameIdMap, coverFlag);

                switch (result) {
                    case ERROR: {
                        errorCount++;
                        break;
                    }
                    case WARNING: {
                        warningCount++;
                        break;
                    }
                    case SUCCESS:
                    default: {
                        successCount++;
                        excelModel.createResult(EnumTaskResult.SUCCESS, "");
                        break;
                    }
                }

            } catch (Exception ex) {
                excelModel.createResult(EnumTaskResult.ERROR, ex.getMessage());
                errorCount++;
            }
            //向webSocket发送进度
            msg = MessageFormat.format("正在执行数据库信息导入任务:{0}/{1}条数据。",
                    Integer.toString(number), Integer.toString(total));
            super.sendWebSocketProgressMsg(uuid, total, number, successCount + warningCount, msg);
            number++;
        }

        return getTaskResultModel(uuid, excelList, total, successCount, errorCount, warningCount);
    }

    /**
     * 获得任务执行结果
     *
     * @param uuid         用户uuid
     * @param excelList    表格数据
     * @param total        总数
     * @param successCount 成功数量
     * @param errorCount   失败数量
     * @param warningCount 警告数量
     * @return 任务执行结果
     * @author yeweiwei
     * @date 2021/12/9 13:33
     */
    private TaskResultModel getTaskResultModel(String uuid, List<DatabaseInfoExcelModel> excelList, int total,
                                               int successCount, int errorCount, int warningCount) {
        String taskLog = MessageFormat.format("共{0}条数据，成功：{1}，失败：{2}，警告：{3}，您可以下载结果文件查看详细结果。",
                Integer.toString(total), Integer.toString(successCount),
                Integer.toString(errorCount), Integer.toString(warningCount));

        //生成文件名
        String resultFileName = System.currentTimeMillis() + "-result" + Constant.Suffix.XLSX_WITH_POINT;

        //上传到文件服务器后的路径
        String resultFilePath = this.saveFile(excelList, resultFileName);

        TaskResultModel taskResultModel = new TaskResultModel();
        //实际导入数量是 成功+警告
        taskResultModel.create(EnumTaskState.执行完成.getValue(), excelList.size(),
                successCount + warningCount, errorCount, resultFileName, resultFilePath, taskLog);
        //发送执行完成的消息
        super.sendWebSocketProgressMsg(uuid, total, total, successCount + warningCount, taskLog);
        return taskResultModel;
    }

    /**
     * 获得名称-id的map集合
     *
     * @param idNameModelList idName集合
     * @return 名称-id的map集合
     * @author yeweiwei
     * @date 2021/11/30 14:29
     */
    private Map<String, Long> getNameIdMap(List<IdNameModel> idNameModelList) {
        Map<String, Long> nameDidMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        nameDidMap.putAll(idNameModelList.stream()
                .collect(Collectors.toMap(IdNameModel::getName, IdNameModel::getId)));
        return nameDidMap;
    }

    /**
     * 保存表格数据
     *
     * @param excelModel        表格数据
     * @param sid               学校id
     * @param userId            操作人id
     * @param dbNameIdSetMap    数据库名称id集合的map
     * @param orderDidSet       学校订购过的数据库id
     * @param aliasDidMap       数据库别名、id集合
     * @param propertyNameIdMap 属性名称idMap
     * @param coverFlag         覆盖标识
     * @return 处理结果
     * @author yeweiwei
     * @date 2021/11/29 17:01
     */
    private EnumTaskResult saveDataBaseInfoData(DatabaseInfoExcelModel excelModel,
                                                Long sid, Long userId, Map<String, Set<Long>> dbNameIdSetMap,
                                                Set<Long> orderDidSet, Map<String, Long> aliasDidMap,
                                                Map<String, Long> propertyNameIdMap, Integer coverFlag) {
        //校验数据
        this.checkExcelData(excelModel);

        // 查询数据库
        String name = FormatUtil.formatValue(excelModel.getName());
        Long did;
        did = aliasDidMap.get(name);
        if (null == did) {
            Set<Long> didSet = dbNameIdSetMap.get(name);
            if (!CollectionUtils.isEmpty(didSet)) {
                if (didSet.size() > 1) {
                    Optional<Long> optional = didSet.stream().filter(orderDidSet::contains).findAny();
                    if (optional.isPresent()) {
                        did = optional.get();
                    }
                } else {
                    did = didSet.iterator().next();
                }
            }
        }

        DatabaseInfoModel baseModel = new DatabaseInfoModel();
        DatabaseInfoInsertModel infoModel = new DatabaseInfoInsertModel();
        // 构造数据库基本信息
        getDatabaseInfoModel(excelModel, propertyNameIdMap, baseModel);

        // 构造数据库详细信息
        getDatabaseMoreInfoModel(excelModel, infoModel);

        DatabaseLevel level = null;
        if (null != did) {
            level = databaseLevelDao.findOneBySidDid(sid, did);
        }

        // 如果did是null，新增数据库基本信息
        if (null == did) {
            // 新增基础信息
            IdNameModel databaseIdName = databaseService.addDatabase(sid, baseModel, userId);
            did = databaseIdName.getId();
        }
        baseModel.setDid(did);

        // 如果level是null，新增数据库详细信息
        if (null == level) {
            // 新增层级信息
            dataBaseInfoService.addDatabaseLevel(sid, userId, did, EnumDataBaseType.总库.getValue(), did);
            // 新增详细信息
            dataBaseInfoService.addOrUpdateDatabaseBaseInfo(sid, did, userId, infoModel);
        } else {
            // 否则判断是否覆盖
            if (coverFlag == EnumTrueFalse.是.getValue()) {
                // 更新层级信息
                if (level.getTotalFlag().equals(EnumDataBaseType.子库.getValue())) {
                    throw new CustomException("检测到该库为子库，无法导入子库");
                }
                // 更新基础信息
                databaseService.updateDatabase(sid, baseModel, userId);
                // 更新详细信息
                dataBaseInfoService.addOrUpdateDatabaseBaseInfo(sid, did, userId, infoModel);
            } else {
                throw new CustomException("因为设置了不覆盖原有数据，所以数据导入失败");
            }
        }

        if (excelModel.getResult() != null) {
            return EnumTaskResult.WARNING;
        }
        return EnumTaskResult.SUCCESS;
    }

    /**
     * 构建详细信息
     *
     * @param excelModel 导入的表格文件
     * @param infoModel  存入数据库的model
     * @author majuehao
     * @date 2022/1/25 16:08
     **/
    private void getDatabaseMoreInfoModel(DatabaseInfoExcelModel excelModel, DatabaseInfoInsertModel infoModel) {

        // 解析是否全文
        EnumTrueFalse fulltextFlag = EnumTrueFalse.getTrueFalse(excelModel.getFulltextFlag());
        if (null == fulltextFlag) {
            throw new CustomException("解析纸电标识出错，请检查数据是否正确");
        }

        infoModel.importFile(excelModel.getArea(), fulltextFlag.getValue(), excelModel.getNatureType());
    }


    /**
     * 构造数据库信息
     *
     * @param excelModel        表格数据
     * @param propertyNameIdMap 属性名称id集合
     * @param databaseInfoModel 数据库信息模型
     * @author yeweiwei
     * @date 2021/11/30 9:43
     */
    private void getDatabaseInfoModel(DatabaseInfoExcelModel excelModel, Map<String, Long> propertyNameIdMap,
                                      DatabaseInfoModel databaseInfoModel) {
        // 解析语种
        Long languageId = EnumLanguageType.getLanguageId(excelModel.getLanguage());
        if (null == languageId) {
            throw new CustomException("解析语种出错，请检查数据是否正确");
        }

        // 解析纸电标识
        Integer paperFlag = EnumPaperType.getPaperType(excelModel.getPaper());
        if (null == paperFlag) {
            throw new CustomException("解析纸电标识出错，请检查数据是否正确");
        }

        // 解析资源类型
        Set<Long> propertySet = Sets.newHashSet();
        String properties = excelModel.getProperties();
        properties = properties.replaceAll("；", Constant.SplitChar.SEMICOLON_CHAR);
        String[] split = properties.split(Constant.SplitChar.SEMICOLON_CHAR);
        StringBuilder warn = new StringBuilder();
        for (String property : split) {
            if (!propertyNameIdMap.containsKey(property)) {
                excelModel.setResult(EnumTaskResult.WARNING.getValue());
                warn.append("无法解析数据库资源类型：").append(property).append(Constant.SplitChar.SEMICOLON_CHAR);
                continue;
            }
            propertySet.add(propertyNameIdMap.get(property));
        }
        if (CollectionUtils.isEmpty(propertySet)) {
            throw new CustomException("解析得到的资源类型为空，请检查数据是否正确");
        }
        excelModel.setErrorMessage(warn.toString());

        //数据库信息
        databaseInfoModel.create(excelModel.getName(), languageId, paperFlag,
                Lists.newArrayList(propertySet));
    }

    /**
     * 校验表格数据，全是必填项
     *
     * @param model 表格数据
     * @author yeweiwei
     * @date 2021/11/29 16:28
     */
    private void checkExcelData(DatabaseInfoExcelModel model) {

        //校验数据库名称
        super.checkIsEmpty(model.getName(), "未能检测到数据库的名称，请检查数据是否正确");
        super.checkLength(model.getName(), Constant.DatabaseEvaluation.NAME_SIZE,
                "数据库名称最大长度为" + Constant.DatabaseEvaluation.NAME_SIZE + "个字符，请检查数据是否正确");
        super.checkPattern(model.getName(), Constant.DatabaseEvaluation.NAME_REGEXP, "数据库名称只能以汉字或字母开头");

        //校验语种
        super.checkIsEmpty(model.getLanguage(), "未能检测到数据库的语种，请检查数据是否正确");
        if (!model.getLanguage().equals(EnumLanguageType.中文.getName())
                && !model.getLanguage().equals(EnumLanguageType.外文.getName())) {
            throw new CustomException("数据库语种只能是中文或外文，请检查数据是否正确");
        }

        //校验纸电
        super.checkIsEmpty(model.getPaper(), "未能检测到数据库的纸电标识，请检查数据是否正确");
        if (!model.getPaper().equals(EnumPaperType.电子.getName())
                && !model.getPaper().equals(EnumPaperType.纸本.getName())) {
            throw new CustomException("数据库纸电标识只能是纸本或电子，请检查数据是否正确");
        }

        //校验全文标识
        super.checkIsEmpty(model.getFulltextFlag(), "未能检测到数据库的全文标识，请检查数据是否正确");
        if (!model.getFulltextFlag().equals(EnumTrueFalse.是.getName())
                && !model.getFulltextFlag().equals(EnumTrueFalse.否.getName())) {
            throw new CustomException("数据库全文标识只能为是或否，请检查数据是否正确");
        }

        //资源类型
        super.checkIsEmpty(model.getProperties(), "未能检测到数据库的资源类型，请检查数据是否正确");

        //所在地区
        super.checkIsEmpty(model.getArea(), "未能检测到数据库的所在地区，请检查数据是否正确");
        super.checkLength(model.getArea(), Constant.DatabaseEvaluation.AREA_SIZE,
                "数据库所在地区最大长度为" + Constant.DatabaseEvaluation.AREA_SIZE + "个字符，请检查数据是否正确");

        //数据库性质
        super.checkIsEmpty(model.getNatureType(), "未能检测到数据库的数据库性质，请检查数据是否正确");
        super.checkLength(model.getNatureType(), Constant.DatabaseEvaluation.NATURE_TYPE_SIZE,
                "数据库性质最大长度为" + Constant.DatabaseEvaluation.NATURE_TYPE_SIZE + "个字符，请检查数据是否正确");
    }

    /**
     * 读取表格
     *
     * @param filePath 文件路径
     * @return 表格数据
     * @author yeweiwei
     * @date 2021/11/29 10:11
     */
    private List<DatabaseInfoExcelModel> readExcelData(String filePath) throws Exception {
        //文件流
        InputStream fileInputStream = MinioFileUtil.getFileInputStream(Constant.MinIoBucketName.ERMS, filePath);

        //如果未能读取到数据，直接抛出异常
        if (fileInputStream == null) {
            throw new Exception(Vm.FILE_NOT_EXIST);
        }

        //获取文件流中的数据
        ExcelUtil.ExcelInfo<DatabaseInfoExcelModel> excelInfo =
                ExcelUtil.readHeadMapListAndData(fileInputStream,
                        EnumExcelHeadlineType.DatabaseInfoImport.getHeadlineNum(),
                        DatabaseInfoExcelModel.class, filePath);
        //读取的表头
        Map<Integer, String> headMap = excelInfo.getHeadMap();
        //校验表头是否有缺失
        EnumExcelHeadlineType.checkHeadlines(EnumExcelHeadlineType.DatabaseInfoImport, headMap);

        //返回读取的数据
        return excelInfo.getData();
    }

    /**
     * 保存文件
     *
     * @param exportList 导出的数据
     * @param fileName   文件名称
     * @return 路径
     * @author yeweiwei
     * @date 2021/11/29 13:54
     */
    private String saveFile(List<DatabaseInfoExcelModel> exportList, String fileName) {

        //当前月
        String nowMonth = DateTimeUtil.localDateToString(LocalDate.now(), Constant.Pattern.MONTH);

        //保存的路径加上月份
        String filePath = TEMP_PATH.concat(nowMonth).concat("/");
        ExcelUtil.saveExcel(exportList, "", "数据库信息导入结果",
                DatabaseInfoExcelModel.class, filePath, fileName);

        //读取本地文件
        File file = new File(filePath.concat(fileName));

        //上传到文件服务器
        return MinioFileUtil.uploadFile(Constant.MinIoBucketName.ERMS, file, EnumTrueFalse.是.getValue());
    }
}
