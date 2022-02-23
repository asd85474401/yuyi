package com.kcidea.erms.service.task;

import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.task.TaskInfoModel;
import com.kcidea.erms.model.task.TaskModel;
import com.kcidea.erms.domain.task.ErmsTask;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/29
 **/
public interface TaskService {

    /**
     * 获取任务类型下拉集合
     *
     * @return 任务类型下拉集合
     * @author majuehao
     * @date 2021/11/29 11:09
     **/
    List<IdNameModel> findTaskTypeDropDownList();

    /**
     * 获取任务状态下拉集合
     *
     * @return 任务状态下拉集合
     * @author majuehao
     * @date 2021/11/29 11:09
     **/
    List<IdNameModel> findTaskStateDropDownList();


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
    PageResult<TaskModel> findTaskList(String taskName, Integer taskType, Integer taskState,
                                       Long sid, Integer pageNum, Integer pageSize);

    /**
     * 删除任务
     *
     * @param taskId 任务id
     * @param sid    学校id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/29 13:44
     **/
    String deleteTask(Long taskId, Long sid);

    /**
     * 查看任务
     *
     * @param taskId 任务id
     * @param sid    学校id
     * @return 任务详情
     * @author majuehao
     * @date 2021/11/29 14:34
     **/
    TaskInfoModel findOneTask(Long taskId, Long sid);

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
    ErmsTask addTask(Long sid, String taskName, Integer taskType, Integer taskState, String taskParamJson,
                     String fileName, String filePath, Long userId, Boolean checkRepeat);

}
