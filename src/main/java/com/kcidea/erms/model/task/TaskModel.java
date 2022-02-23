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
public class TaskModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型
     */
    private Integer taskType;

    /**
     * 任务类型名称
     */
    private String taskTypeName;

    /**
     * 任务状态：0=未执行，1=执行中，2=正常 3=异常
     */
    private Integer taskState;

    /**
     * 任务状态名称
     */
    private String taskStateName;

    /**
     * 成功数量
     */
    private Integer successCount;

    /**
     * 失败数量
     */
    private Integer errorCount;

    /**
     * 上传的文件名
     */
    private String fileName;

    /**
     * 上传的路径
     */
    private String filePath;

    /**
     * 结果文件名
     */
    private String resultFileName;

    /**
     * 结果路径
     */
    private String resultFilePath;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
