package com.kcidea.erms.domain.feedback;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/27 9:53
 **/
@Data
public class QuestionFeedbackType implements Serializable {
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
     * 反馈类型名称
     */
    private String name;

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

    public QuestionFeedbackType create(Long sId, String name, Long createdBy, LocalDateTime createdTime) {
        this.sId = sId;
        this.name = name;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        return this;
    }

    private static final long serialVersionUID = 1L;
}