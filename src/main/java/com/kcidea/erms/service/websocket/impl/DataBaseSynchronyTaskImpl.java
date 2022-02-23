package com.kcidea.erms.service.websocket.impl;

import com.alibaba.fastjson.JSON;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.util.RightsUtil;
import com.kcidea.erms.dao.database.DatabaseEvaluationDao;
import com.kcidea.erms.dao.ers.SchoolDatabaseListDao;
import com.kcidea.erms.dao.ers.VdatabaseDao;
import com.kcidea.erms.dao.task.ErmsTaskDao;
import com.kcidea.erms.dao.task.ErmsTaskRecordDao;
import com.kcidea.erms.domain.database.DatabaseEvaluation;
import com.kcidea.erms.domain.task.ErmsTask;
import com.kcidea.erms.domain.task.ErmsTaskRecord;
import com.kcidea.erms.enums.common.EnumWebSocketApi;
import com.kcidea.erms.enums.database.EnumEvaluationResult;
import com.kcidea.erms.enums.fund.EnumOrderType;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.task.EnumTaskState;
import com.kcidea.erms.enums.task.EnumTaskType;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.common.TaskParamModel;
import com.kcidea.erms.model.menu.MenuModel;
import com.kcidea.erms.model.task.TaskResultModel;
import com.kcidea.erms.model.user.UserModel;
import com.kcidea.erms.service.menu.MenuService;
import com.kcidea.erms.service.websocket.WebSocketTaskBaseService;
import com.kcidea.erms.service.websocket.WebSocketTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/25
 **/
@Slf4j
@Service
public class DataBaseSynchronyTaskImpl extends WebSocketTaskBaseService implements WebSocketTaskService {

    @Resource
    private DatabaseEvaluationDao databaseEvaluationDao;

    @Resource
    private SchoolDatabaseListDao schoolDatabaseListDao;

    @Resource
    private ErmsTaskDao taskDao;

    @Resource
    private ErmsTaskRecordDao taskRecordDao;

    @Resource
    private VdatabaseDao vdatabaseDao;

    @Resource
    private MenuService menuService;

    /**
     * 获取WebSocketApi工厂
     *
     * @return WebSocketApi工厂
     * @author huxubin
     * @date 2021/11/25 16:11
     */
    @Override
    public EnumWebSocketApi getWebSocketApi() {
        return EnumWebSocketApi.数据库评估列表同步;
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
        // 判断权限
        if (!RightsUtil.checkAdminMenu(menuList, EnumMenu.数据库评估管理.getName(), EnumUserAction.新增)) {
            String errMsg = MessageFormat.format("很抱歉，您没有{0}权限，无法进行此操作",
                    EnumUserAction.新增.getName());
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
        ErmsTask task = super.addTask(userModel, jsonData, EnumTaskType.数据库评估列表同步);
        ErmsTaskRecord taskRecord = new ErmsTaskRecord().setTaskId(task.getId()).setStartTime(LocalDateTime.now());
        TaskParamModel taskParamModel = JSON.parseObject(task.getTaskParams(), TaskParamModel.class);
        try {
            TaskResultModel taskResult =
                    this.executeSynchronyTask(uuid, userModel.getSid(), userModel.getId(), taskParamModel.getVYear());

            //更新任务状态
            task.updateByTaskResult(taskResult);

            //更新任务结果日志
            taskRecord.setTaskLog(taskResult.getTaskLog());
        } catch (Exception ex) {
            log.error("执行数据库评估列表同步任务失败，原因：" + ex.getMessage());
            //更新任务状态
            task.setTaskState(EnumTaskState.执行失败.getValue());
            //更新任务结果日志
            taskRecord.setTaskLog(Vm.TASK_ERROR.concat(ex.getMessage()));

            //发送执行失败的消息
            super.sendWebSocketProgressMsg(uuid, null, null, null,
                    "执行数据库评估列表同步任务失败。");
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
     * 执行同步任务
     *
     * @param uuid       操作人uuid
     * @param sid        学校id
     * @param userId     操作人id
     * @param sourceYear 来源年份
     * @return 执行任务结果
     * @author yeweiwei
     * @date 2021/11/30 19:20
     */
    private TaskResultModel executeSynchronyTask(String uuid, Long sid, Long userId, Integer sourceYear) {

        int taskState = EnumTaskState.执行完成.getValue();

        int targetYear = sourceYear + 1;

        //查询来源年份学校订购的数据库
        Set<Long> orderDidSet = schoolDatabaseListDao.findOrderDidSetBySidYear(sid, sourceYear);

        //查询目标年份已经评估的数据库id集合
        Set<Long> evaluationDidSet = databaseEvaluationDao.findDidSetBySidYear(sid, targetYear);

        LocalDateTime now = LocalDateTime.now();

        int number = 1;
        int successCount = 0;
        int errorCount = 0;

        // 发送即将开始的消息
        int totalCount = orderDidSet.size();
        String msg = MessageFormat.format("即将开始执行数据库评估同步任务，共{0}条数据。", totalCount);
        super.sendWebSocketProgressMsg(uuid, totalCount, 0, successCount, msg);
        StringBuilder errorBuilder = new StringBuilder();
        for (Long did : orderDidSet) {
            msg = MessageFormat.format("正在执行数据库评估同步任务:{0}/{1}条数据。", number, totalCount);
            try {
                if (evaluationDidSet.contains(did)) {
                    String name = vdatabaseDao.findNameBySidDid(sid, did);
                    errorBuilder.append("</br>");
                    errorBuilder.append("同步[").append(name).append("]数据库的评估信息失败：")
                            .append(targetYear).append("年已有此数据库的评估记录。");
                    errorCount++;
                    //向webSocket发送进度
                    super.sendWebSocketProgressMsg(uuid, totalCount, number, successCount, msg);
                    number++;
                    continue;
                }
                DatabaseEvaluation newEvaluation = new DatabaseEvaluation();
                newEvaluation.create(sid, did, targetYear, EnumOrderType.续订数据库.getValue(),
                        EnumEvaluationResult.未评估.getValue(), userId, now);
                databaseEvaluationDao.insert(newEvaluation);
                successCount++;
            } catch (Exception e) {
                log.error("同步一条数据库评估失败：原因" + e.getMessage(), e);
                errorCount++;
            }
            //向webSocket发送进度
            super.sendWebSocketProgressMsg(uuid, totalCount, number, successCount, msg);
            number++;
        }

        //执行完成
        String taskLog = MessageFormat.format("共{0}条数据，成功：{1}，失败：{2}。",
                Integer.toString(totalCount), Integer.toString(successCount), Integer.toString(errorCount));
        taskLog = taskLog.concat(errorBuilder.toString());
        TaskResultModel taskResultModel = new TaskResultModel();
        taskResultModel.create(taskState, totalCount, successCount, errorCount,
                null, null, taskLog);

        //发送执行完成的消息
        String successMsg = MessageFormat.format("共{0}条数据，成功：{1}，失败：{2}，您可以前往任务列表查看详细结果。",
                Integer.toString(totalCount), Integer.toString(successCount), Integer.toString(errorCount));
        super.sendWebSocketProgressMsg(uuid, totalCount, totalCount, successCount, successMsg);
        return taskResultModel;
    }

}
