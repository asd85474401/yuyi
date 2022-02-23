package com.kcidea.erms.domain.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kcidea.erms.model.task.TaskResultModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/29
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ErmsTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 学校id
     */
    private Long sId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型
     */
    private Integer taskType;

    /**
     * 任务参数
     */
    private String taskParams;

    /**
     * 任务状态：0=未执行，1=执行中，2=正常 3=异常
     */
    private Integer taskState;

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
     * 成功数量
     */
    private Integer successCount;

    /**
     * 失败数量
     */
    private Integer errorCount;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    private Long updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    public void updateByTaskResult(TaskResultModel taskResult) {
        this.taskState = taskResult.getTaskState();
        this.successCount = taskResult.getSuccessCount();
        this.errorCount = taskResult.getErrorCount();
        this.resultFileName = taskResult.getResultFileName();
        this.resultFilePath = taskResult.getResultFilePath();
    }

    public ErmsTask create(Long sId, String taskName, Integer taskType, String taskParams, Integer taskState,
                           String fileName, String filePath, Long createdBy, LocalDateTime createdTime) {
        this.sId = sId;
        this.taskName = taskName;
        this.taskType = taskType;
        this.taskParams = taskParams;
        this.taskState = taskState;
        this.fileName = fileName;
        this.filePath = filePath;
        this.createdBy = createdBy;
        this.createdTime = createdTime;

        return this;
    }
}