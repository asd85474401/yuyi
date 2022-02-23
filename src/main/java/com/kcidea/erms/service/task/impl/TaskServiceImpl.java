package com.kcidea.erms.service.task.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.dao.task.ErmsTaskDao;
import com.kcidea.erms.dao.task.ErmsTaskRecordDao;
import com.kcidea.erms.domain.task.ErmsTask;
import com.kcidea.erms.domain.task.ErmsTaskRecord;
import com.kcidea.erms.enums.task.EnumTaskState;
import com.kcidea.erms.enums.task.EnumTaskType;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.task.TaskInfoModel;
import com.kcidea.erms.model.task.TaskModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.task.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/29
 **/
@Service
public class TaskServiceImpl extends BaseService implements TaskService {

    @Resource
    private ErmsTaskDao ermsTaskDao;

    @Resource
    private ErmsTaskRecordDao ermsTaskRecordDao;

    /**
     * 获取任务类型下拉集合
     *
     * @return 任务类型下拉集合
     * @author majuehao
     * @date 2021/11/29 11:09
     **/
    @Override
    public List<IdNameModel> findTaskTypeDropDownList() {
        return EnumTaskType.getDropDownList();
    }

    /**
     * 获取任务状态下拉集合
     *
     * @return 任务状态下拉集合
     * @author majuehao
     * @date 2021/11/29 11:09
     **/
    @Override
    public List<IdNameModel> findTaskStateDropDownList() {
        return EnumTaskState.getDropDownList();
    }

    /**
     * 查询任务列表
     *
     * @param taskName  任务名称
     * @param taskType  任务类型
     * @param taskState 任务状态
     * @param sid       学校id
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @return 任务列表
     * @author majuehao
     * @date 2021/11/29 13:21
     **/
    @Override
    public PageResult<TaskModel> findTaskList(String taskName, Integer taskType, Integer taskState, Long sid,
                                              Integer pageNum, Integer pageSize) {
        // 校验参数
        super.checkPageParam(pageNum, pageSize);
        super.checkSid(sid);

        // 分页参数
        Page<TaskModel> page = new Page<>(pageNum, pageSize);

        // 查询数据
        List<TaskModel> list = ermsTaskDao.findListBySidNameTypeStatePage(sid, taskName, taskType, taskState, page);

        // 组装数据
        for (TaskModel model : list) {
            // 任务类型
            EnumTaskType enumTaskType = EnumTaskType.getEnumTaskType(model.getTaskType());
            model.setTaskTypeName(enumTaskType == null ? "" : enumTaskType.getName());

            // 任务状态
            EnumTaskState enumTaskState = EnumTaskState.getEnumTaskState(model.getTaskState());
            model.setTaskStateName(enumTaskState == null ? "" : enumTaskState.getName());
        }

        // 分页返回
        PageResult<TaskModel> result = new PageResult<>();
        result.success(list, page.getTotal());
        return result;
    }

    /**
     * 删除任务
     *
     * @param taskId 任务id
     * @param sid    学校id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/29 13:44
     **/
    @Override
    public String deleteTask(Long taskId, Long sid) {
        // 校验参数
        ErmsTask ermsTask = ermsTaskDao.selectById(taskId);

        if (ermsTask == null) {
            throw new CustomException(Vm.NO_DATA);
        }

        if (!ermsTask.getSId().equals(sid)) {
            throw new CustomException(Vm.ERROR_REQUEST);
        }

        if (!ermsTask.getTaskState().equals(EnumTaskState.未执行.getValue())) {
            throw new CustomException("只能删除未执行的任务");
        }

        ermsTaskDao.deleteById(ermsTask);

        // 级联删除任务记录
        ermsTaskRecordDao.deleteByTaskId(taskId);

        return Vm.DELETE_SUCCESS;
    }

    /**
     * 查看任务
     *
     * @param taskId 任务id
     * @param sid    学校id
     * @return 任务详情
     * @author majuehao
     * @date 2021/11/29 14:34
     **/
    @Override
    public TaskInfoModel findOneTask(Long taskId, Long sid) {
        // 校验参数
        super.checkSid(sid);

        // 查询数据，校验参数
        ErmsTaskRecord ermsTaskRecord = ermsTaskRecordDao.findOneByTaskId(taskId);
        if (ermsTaskRecord == null) {
            throw new CustomException(Vm.NO_DATA);
        }
        ErmsTask ermsTask = ermsTaskDao.selectById(ermsTaskRecord.getTaskId());
        if (ermsTask == null) {
            throw new CustomException(Vm.NO_DATA);
        }
        if (!ermsTask.getSId().equals(sid)) {
            throw new CustomException(Vm.ERROR_REQUEST);
        }

        // 设置参数返回
        TaskInfoModel model = new TaskInfoModel();
        BeanUtils.copyProperties(ermsTaskRecord, model);
        model.setTaskName(ermsTask.getTaskName());
        model.setTaskState(ermsTask.getTaskState());

        // 任务状态
        EnumTaskState enumTaskState = EnumTaskState.getEnumTaskState(ermsTask.getTaskState());
        model.setTaskStateName(enumTaskState == null ? "" : enumTaskState.getName());

        return model;
    }

    /**
     * 添加任务
     *
     * @param sid           学校id
     * @param taskName      任务名称
     * @param taskType      任务类型
     * @param taskState     任务状态
     * @param taskParamJson 任务参数Json
     * @param fileName      上传文件名称
     * @param filePath      上传文件路径
     * @param userId        任务创建人id
     * @param checkRepeat   是否检查重复任务
     * @return 新增的任务
     * @author yeweiwei
     * @date 2021/11/29 09:15
     */
    @Override
    public ErmsTask addTask(Long sid, String taskName, Integer taskType, Integer taskState, String taskParamJson,
                            String fileName, String filePath, Long userId, Boolean checkRepeat) {
        ErmsTask task = new ErmsTask();
        if (null == taskType) {
            throw new CustomException("任务类型不能为空");
        }
        if (Strings.isNullOrEmpty(taskParamJson)) {
            throw new CustomException("任务参数不能为空");
        }

        if (null == userId) {
            throw new CustomException("任务创建人id不能为空");
        }

        //新增的任务只能是未执行或正在执行
        if (null == taskState) {
            taskState = EnumTaskState.未执行.getValue();
        }
        if (!taskState.equals(EnumTaskState.未执行.getValue()) && !taskState.equals(EnumTaskState.正在执行.getValue())) {
            throw new CustomException("任务状态错误");
        }

        //如果需要校验重复，需要先查询是否有执行中或者等待执行的任务
        if (checkRepeat) {
            int count = ermsTaskDao.findUnfinishedTask(taskType);
            if (count > 0) {
                throw new CustomException("很抱歉，检测到已有相同任务尚未执行完成，无法保存");
            }
        }

        task.create(sid, taskName, taskType, taskParamJson, taskState, fileName, filePath, userId, LocalDateTime.now());
        ermsTaskDao.insert(task);

        return task;
    }
}