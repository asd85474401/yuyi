package com.kcidea.erms.service.websocket.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.util.*;
import com.kcidea.erms.dao.ers.SchoolDatabaseAliasDao;
import com.kcidea.erms.dao.ers.SchoolDatabaseListDao;
import com.kcidea.erms.dao.ers.VdatabaseDao;
import com.kcidea.erms.dao.feedback.QuestionFeedbackDao;
import com.kcidea.erms.dao.feedback.QuestionFeedbackTypeDao;
import com.kcidea.erms.dao.task.ErmsTaskDao;
import com.kcidea.erms.dao.task.ErmsTaskRecordDao;
import com.kcidea.erms.domain.feedback.QuestionFeedbackType;
import com.kcidea.erms.domain.task.ErmsTask;
import com.kcidea.erms.domain.task.ErmsTaskRecord;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.common.EnumWebSocketApi;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.task.EnumExcelHeadlineType;
import com.kcidea.erms.enums.task.EnumTaskResult;
import com.kcidea.erms.enums.task.EnumTaskState;
import com.kcidea.erms.enums.task.EnumTaskType;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.common.WebSocketTaskModel;
import com.kcidea.erms.model.feedback.FeedbackInfoModel;
import com.kcidea.erms.model.menu.MenuModel;
import com.kcidea.erms.model.task.FeedbackExcelModel;
import com.kcidea.erms.model.task.TaskResultModel;
import com.kcidea.erms.model.user.UserModel;
import com.kcidea.erms.service.feedback.FeedbackDisposeService;
import com.kcidea.erms.service.menu.MenuService;
import com.kcidea.erms.service.task.TaskService;
import com.kcidea.erms.service.websocket.WebSocketTaskBaseService;
import com.kcidea.erms.service.websocket.WebSocketTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/21
 **/
@Slf4j
@Service
public class UserFeedbackListImportTaskImpl extends WebSocketTaskBaseService implements WebSocketTaskService {

    @Value("${my-config.temp-path}")
    private String TEMP_PATH;

    @Resource
    private ErmsTaskDao taskDao;

    @Resource
    private ErmsTaskRecordDao taskRecordDao;

    @Resource
    private VdatabaseDao vdatabaseDao;

    @Resource
    private SchoolDatabaseListDao schoolDatabaseListDao;

    @Resource
    private SchoolDatabaseAliasDao schoolDatabaseAliasDao;

    @Resource
    private FeedbackDisposeService feedbackService;

    @Resource
    private QuestionFeedbackDao questionFeedbackDao;

    @Resource
    private TaskService taskService;

    @Resource
    private MenuService menuService;

    @Resource
    private QuestionFeedbackTypeDao questionFeedbackTypeDao;

    /**
     * 获取WebSocketApi工厂
     *
     * @return WebSocketApi工厂
     * @author majuehao
     * @date 2021/11/25 16:11
     */
    @Override
    public EnumWebSocketApi getWebSocketApi() {
        return EnumWebSocketApi.用户反馈处理列表导入;
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
        if (!RightsUtil.checkAdminMenu(menuList, EnumMenu.问题反馈.getName(), EnumUserAction.导入)) {
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
     * @author majuehao
     * @date 2021/12/22 15:18
     **/
    @Override
    public void executeTask(String uuid, UserModel userModel, Object jsonData) {
        // 解析jsonData
        WebSocketTaskModel webSocketTaskModel = JSONObject.toJavaObject((JSON) jsonData, WebSocketTaskModel.class);
        String fileName = webSocketTaskModel.getFileName();
        String filePath = webSocketTaskModel.getFilePath();

        // 任务名称
        String taskName = DateTimeUtil.localDateTimeToString(LocalDateTime.now(), Constant.Pattern.DATE_NAME)
                .concat(Constant.SplitChar.LINE_CHAR).concat(EnumTaskType.用户反馈处理列表导入.getName());

        ErmsTask task = taskService.addTask(userModel.getSid(), taskName, EnumTaskType.用户反馈处理列表导入.getValue(),
                EnumTaskState.正在执行.getValue(), JSON.toJSONString(taskName), fileName, filePath, userModel.getId(), Boolean.FALSE);
        ErmsTaskRecord taskRecord = new ErmsTaskRecord().setTaskId(task.getId()).setStartTime(LocalDateTime.now());
        try {
            TaskResultModel taskResult =
                    this.executeImportTask(task, userModel.getSid(), userModel.getId(), uuid);

            //更新任务状态
            task.updateByTaskResult(taskResult);

            //更新任务结果日志
            taskRecord.setTaskLog(taskResult.getTaskLog());
        } catch (Exception ex) {
            log.error("执行用户反馈处理列表导入任务失败，原因：" + ex.getMessage());
            //更新任务状态
            task.setTaskState(EnumTaskState.执行失败.getValue());
            //更新任务结果
            taskRecord.setTaskLog(Vm.TASK_ERROR.concat(ex.getMessage()));

            //发送执行失败的消息
            super.sendWebSocketProgressMsg(uuid, null, null, null,
                    "执行用户反馈处理列表导入任务失败。");
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
     * @param task   任务
     * @param sid    学校id
     * @param userId 操作人id
     * @param uuid   操作人uuid
     * @return 执行结果
     * @author yeweiwei
     * @date 2021/11/29 14:53
     */
    private TaskResultModel executeImportTask(ErmsTask task, Long sid,
                                              Long userId, String uuid) throws Exception {
        //读取文件
        List<FeedbackExcelModel> excelList = this.readExcelData(task.getFilePath());
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

        //数据库id别名集合
        Map<String, Long> aliasDidMap = getNameIdMap(schoolDatabaseAliasDao.findIdNameListBySid(sid));

        System.out.println("基础数据准备完成，耗时：" + (System.currentTimeMillis() - t1));

        // 发送开始执行任务的消息
        String msg = MessageFormat.format("即将开始执行用户反馈处理列表导入任务，共{0}条数据。", total);
        super.sendWebSocketProgressMsg(uuid, total, 0, successCount, msg);

        //处理文件数据
        for (FeedbackExcelModel model : excelList) {
            try {
                EnumTaskResult result = this.saveFeedbackData(model, sid, userId, dbNameIdSetMap,
                        orderDidSet, aliasDidMap);

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
                        model.createResult(EnumTaskResult.SUCCESS, "");
                        break;
                    }
                }

            } catch (Exception ex) {
                model.createResult(EnumTaskResult.ERROR, ex.getMessage());
                errorCount++;
            }
            //向webSocket发送进度
            msg = MessageFormat.format("正在执行用户反馈处理列表导入任务:{0}/{1}条数据。",
                    Integer.toString(number), Integer.toString(total));
            super.sendWebSocketProgressMsg(uuid, total, number, successCount + warningCount, msg);
            number++;
        }

        return getTaskResultModel(uuid, excelList, total, successCount, errorCount, warningCount);
    }


    /**
     * 读取表格
     *
     * @param filePath 文件路径
     * @return 表格数据
     * @author yeweiwei
     * @date 2021/11/29 10:11
     */
    private List<FeedbackExcelModel> readExcelData(String filePath) throws Exception {
        //文件流
        InputStream fileInputStream = MinioFileUtil.getFileInputStream(Constant.MinIoBucketName.ERMS, filePath);

        //如果未能读取到数据，直接抛出异常
        if (fileInputStream == null) {
            throw new Exception(Vm.FILE_NOT_EXIST);
        }

        //获取文件流中的数据
        ExcelUtil.ExcelInfo<FeedbackExcelModel> excelInfo =
                ExcelUtil.readHeadMapListAndData(fileInputStream,
                        EnumExcelHeadlineType.UserFeedbackListImportTaskImpl.getHeadlineNum(),
                        FeedbackExcelModel.class, filePath);
        //读取的表头
        Map<Integer, String> headMap = excelInfo.getHeadMap();
        //校验表头是否有缺失
        EnumExcelHeadlineType.checkHeadlines(EnumExcelHeadlineType.UserFeedbackListImportTaskImpl, headMap);

        //返回读取的数据
        return excelInfo.getData();
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
     * @param model          表格数据
     * @param sid            学校id
     * @param userId         操作人id
     * @param dbNameIdSetMap 数据库名称id集合的map
     * @param orderDidSet    学校订购过的数据库id
     * @param aliasDidMap    数据库别名、id集合
     * @return 处理结果
     * @author yeweiwei
     * @date 2021/11/29 17:01
     */
    private EnumTaskResult saveFeedbackData(FeedbackExcelModel model,
                                            Long sid, Long userId, Map<String, Set<Long>> dbNameIdSetMap,
                                            Set<Long> orderDidSet, Map<String, Long> aliasDidMap) {
        //校验数据
        this.checkExcelData(model, dbNameIdSetMap, aliasDidMap);

        // 查询数据库
        String name = FormatUtil.formatValue(model.getDatabaseName());
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

        // 反馈时间
        LocalDateTime feedbackTime = DateTimeUtil.strToDate(model.getFeedbackTime());
        // 反馈类型
        Long typeId = getFeedbackType(sid, model.getType(), userId);

        // 回复时间
        LocalDateTime answerTime = DateTimeUtil.strToDate(model.getAnswerTime());

        // 构造反馈处理
        FeedbackInfoModel feedbackInfoModel = new FeedbackInfoModel().create(
                model.getFeedbackTitle(), model.getName(), model.getIdentity(), model.getUnit(), feedbackTime, model.getEmail(), model.getPhone(), typeId, did,
                model.getFeedbackContent(), model.getAnswerName(), answerTime, model.getAnswerContent(),
                EnumTrueFalse.否.getValue());

        // 查询是否重复导入
        checkIsImport(feedbackInfoModel);

        feedbackService.addOrUpdateFeedback(sid, userId, feedbackInfoModel, null);

        if (model.getResult() != null) {
            return EnumTaskResult.WARNING;
        }
        return EnumTaskResult.SUCCESS;
    }

    /**
     * 获取反馈类型
     *
     * @param sid      学校id
     * @param typeName 反馈名称
     * @param userId   用户id
     * @return 反馈类型id
     * @author majuehao
     * @date 2021/12/27 10:12
     **/
    private Long getFeedbackType(Long sid, String typeName, Long userId) {
        // 首尾去空格
        typeName = FormatUtil.formatValue(typeName);
        // 查询id
        Long typeId = questionFeedbackTypeDao.findTypeIdBySidName(sid, typeName);
        // 查不到说明没有该类型，新增该类型
        if (typeId == null) {
            QuestionFeedbackType questionFeedbackType = new QuestionFeedbackType().create(
                    sid, typeName, userId, LocalDateTime.now());
            questionFeedbackTypeDao.insert(questionFeedbackType);
            return questionFeedbackType.getId();
        } else {
            return typeId;
        }
    }

    /**
     * 查询是否重复导入
     *
     * @param feedbackInfoModel 反馈
     * @author majuehao
     * @date 2021/12/21 18:28
     **/
    private void checkIsImport(FeedbackInfoModel feedbackInfoModel) {
        List<FeedbackInfoModel> list = questionFeedbackDao.findOneByFeedbackTitle(feedbackInfoModel.getFeedbackTitle());
        if (list.size() > 0) {
            for (FeedbackInfoModel model : list) {
                if (model.equals(feedbackInfoModel)) {
                    throw new CustomException("该条反馈已经导入，无须再次导入");
                }
            }
        }
    }

    /**
     * 校验表格数据
     *
     * @param model          表格数据
     * @param dbNameIdSetMap 数据库名称id集合的map
     * @param aliasDidMap    数据库别名、id集合
     * @author majuehao
     * @date 2021/12/21 16:41
     **/
    private void checkExcelData(FeedbackExcelModel model, Map<String, Set<Long>> dbNameIdSetMap,
                                Map<String, Long> aliasDidMap) {

        // 校验反馈人
        super.checkIsEmpty(model.getName(), "未能检测到反馈人，请检查数据是否正确");
        super.checkLength(model.getName(), Constant.Feedback.SIZE_HUNDRED,
                "反馈人名称最大长度为" + Constant.Feedback.SIZE_HUNDRED + "个字符，请检查数据是否正确");

        // 校验用户身份
        if (model.getIdentity() != null) {
            super.checkLength(model.getName(), Constant.Feedback.SIZE_HUNDRED,
                    "用户身份最大长度为" + Constant.Feedback.SIZE_HUNDRED + "个字符，请检查数据是否正确");
        }

        // 校验所属单位
        if (model.getUnit() != null) {
            super.checkLength(model.getName(), Constant.Feedback.SIZE_HUNDRED_FIFTY,
                    "所属单位最大长度为" + Constant.Feedback.SIZE_HUNDRED_FIFTY + "个字符，请检查数据是否正确");
        }

        // 校验邮箱
        if (model.getEmail() != null) {
            super.checkLength(model.getEmail(), Constant.Feedback.SIZE_HUNDRED_FIFTY,
                    "邮箱最大长度为" + Constant.Feedback.SIZE_HUNDRED_FIFTY + "个字符，请检查数据是否正确");
            super.checkPattern(model.getEmail(), Constant.Feedback.EMAIL, "邮箱格式不正确");
        }

        // 校验联系电话
        if (model.getPhone() != null) {
            super.checkPattern(model.getPhone(), Constant.Feedback.PHONE, "联系电话格式不正确");
        }

        // 校验反馈时间
        super.checkIsEmpty(model.getFeedbackTime(), "未能检测到反馈时间，请检查数据是否正确");
        super.checkTimeFormat(model.getFeedbackTime(), "反馈时间格式不正确，请按照模板格式填写");


        // 校验反馈类型
        super.checkIsEmpty(model.getType(), "未能检测到反馈类型，请检查数据是否正确");

        // 校验数据库名称
        String name = FormatUtil.formatValue(model.getDatabaseName());
        Long did = aliasDidMap.get(name);
        Set<Long> didSet = dbNameIdSetMap.get(name);
        if (null == did && CollectionUtils.isEmpty(didSet)) {
            throw new CustomException("该数据库不存在，请检查数据是否正确");
        }

        // 校验反馈标题
        super.checkIsEmpty(model.getFeedbackTitle(), "未能检测到反馈标题，请检查数据是否正确");
        super.checkLength(model.getFeedbackTitle(), Constant.Feedback.SIZE_HUNDRED_FIFTY,
                "反馈标题最大长度为" + Constant.Feedback.SIZE_HUNDRED_FIFTY + "个字符，请检查数据是否正确");

        // 校验反馈内容
        super.checkIsEmpty(model.getFeedbackContent(), "未能检测到反馈内容，请检查数据是否正确");
        super.checkLength(model.getFeedbackContent(), Constant.Feedback.SIZE_THOUSAND,
                "反馈内容最大长度为" + Constant.Feedback.SIZE_THOUSAND + "个字符，请检查数据是否正确");

        // 校验回复人
        super.checkIsEmpty(model.getAnswerName(), "未能检测到回复人，请检查数据是否正确");
        super.checkLength(model.getAnswerName(), Constant.Feedback.SIZE_HUNDRED,
                "回复人最大长度为" + Constant.Feedback.SIZE_HUNDRED + "个字符，请检查数据是否正确");

        // 校验回复时间
        super.checkIsEmpty(model.getAnswerTime(), "未能检测到回复时间，请检查数据是否正确");
        super.checkTimeFormat(model.getAnswerTime(), "回复时间格式不正确，请按照模板格式填写");

        // 校验回复内容
        super.checkIsEmpty(model.getAnswerContent(), "未能检测到回复内容，请检查数据是否正确");
        super.checkLength(model.getAnswerContent(), Constant.Feedback.SIZE_THOUSAND,
                "回复内容最大长度为" + Constant.Feedback.SIZE_THOUSAND + "个字符，请检查数据是否正确");
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
    private TaskResultModel getTaskResultModel(String uuid, List<FeedbackExcelModel> excelList, int total,
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
     * 保存文件
     *
     * @param exportList 导出的数据
     * @param fileName   文件名称
     * @return 路径
     * @author yeweiwei
     * @date 2021/11/29 13:54
     */
    private String saveFile(List<FeedbackExcelModel> exportList, String fileName) {

        //当前月
        String nowMonth = DateTimeUtil.localDateToString(LocalDate.now(), Constant.Pattern.MONTH);

        //保存的路径加上月份
        String filePath = TEMP_PATH.concat(nowMonth).concat("/");
        ExcelUtil.saveExcel(exportList, "", "用户反馈处理列表导入结果",
                FeedbackExcelModel.class, filePath, fileName);

        //读取本地文件
        File file = new File(filePath.concat(fileName));

        //上传到文件服务器
        return MinioFileUtil.uploadFile(Constant.MinIoBucketName.ERMS, file, EnumTrueFalse.是.getValue());
    }
}
