package com.kcidea.erms.dao.task;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kcidea.erms.domain.task.ErmsTask;
import com.kcidea.erms.model.task.TaskModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/29
 **/
public interface ErmsTaskDao extends BaseMapper<ErmsTask> {
    /**
     * 根据任务类型查询未完成的任务数量
     *
     * @param taskType 任务类型
     * @return 未完成的任务数量
     * @author yeweiwei
     * @date 2021/11/29 9:25
     */
    int findUnfinishedTask(@Param("taskType") Integer taskType);

    /**
     * 查询任务列表
     *
     * @param sid       学校id
     * @param taskName  任务名称
     * @param taskType  任务类型
     * @param taskState 任务状态
     * @param page      分页
     * @return 任务列表
     * @author majuehao
     * @date 2021/11/29 13:21
     **/
    List<TaskModel> findListBySidNameTypeStatePage(@Param("sid") Long sid, @Param("taskName") String taskName,
                                                   @Param("taskType") Integer taskType,
                                                   @Param("taskState") Integer taskState,
                                                   @Param("page") Page<TaskModel> page);

}