package com.kcidea.erms.dao.task;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.task.ErmsTaskRecord;
import org.apache.ibatis.annotations.Param;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/29 10:40
 **/
public interface ErmsTaskRecordDao extends BaseMapper<ErmsTaskRecord> {

    /**
     * 根据任务id，查找任务详情
     *
     * @param taskId 任务id
     * @return 任务详情
     * @author majuehao
     * @date 2021/12/1 17:01
     **/
    ErmsTaskRecord findOneByTaskId(@Param("taskId") Long taskId);

    /**
     * 根据任务id，删除任务记录
     *
     * @param taskId 任务id
     * @author majuehao
     * @date 2021/12/3 16:04
     **/
    void deleteByTaskId(@Param("taskId") Long taskId);
}
