package com.kcidea.erms.model.task;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/29
 **/
@Data
public class TaskInfoModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务状态：0=未执行，1=执行中，2=正常 3=异常
     */
    private Integer taskState;

    /**
     * 任务状态名称
     */
    private String taskStateName;

    /**
     * 任务开始时间
     */
    private LocalDateTime startTime;

    /**
     * 任务结束时间
     */
    private LocalDateTime endTime;

    /**
     * 任务日志
     */
    private String taskLog;

}
