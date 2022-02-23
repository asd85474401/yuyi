package com.kcidea.erms.model.task;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/8/10
 **/
@Data
@Accessors(chain = true)
public class TaskResultModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务执行结果状态
     */
    private Integer taskState;

    /**
     * 文件里面的总量
     */
    private Integer totalCount;

    /**
     * 成功数量
     */
    private Integer successCount;

    /**
     * 失败数量
     */
    private Integer errorCount;

    /**
     * 返回的文件名
     */
    private String resultFileName;

    /**
     * 返回的文件路径
     */
    private String resultFilePath;

    /**
     * 任务执行结果
     */
    private String taskLog;

    public TaskResultModel create(Integer taskState, Integer totalCount, Integer successCount, Integer errorCount,
                                                            String resultFileName, String resultFilePath, String taskLog) {
        this.taskState = taskState;
        this.totalCount = totalCount;
        this.successCount = successCount;
        this.errorCount = errorCount;
        this.resultFileName = resultFileName;
        this.resultFilePath = resultFilePath;
        this.taskLog = taskLog;
        return this;
    }

    public TaskResultModel create(Integer taskState, Integer totalCount, Integer successCount, Integer errorCount,
                                                            String taskLog) {
        this.taskState = taskState;
        this.totalCount = totalCount;
        this.successCount = successCount;
        this.errorCount = errorCount;
        this.taskLog = taskLog;
        return this;
    }
}
