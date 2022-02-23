package com.kcidea.erms.domain.feedback;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/20 9:53
 **/
@Data
public class QuestionFeedback implements Serializable {
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
     * 数据库id
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long dId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户身份
     */
    private String identity;

    /**
     * 用户单位
     */
    private String unit;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 反馈类型
     */
    private Long typeId;

    /**
     * 反馈人的IP
     */
    private String ip;

    /**
     * 反馈标题
     */
    private String feedbackTitle;

    /**
     * 反馈时间
     */
    private LocalDateTime feedbackTime;

    /**
     * 反馈内容
     */
    private String feedbackContent;

    /**
     * 回复状态（0=未回复 1=已回复）
     */
    private Integer replyState;

    /**
     * 审核状态(0=待审核 1=审核通过  2=审核未通过)
     */
    private Integer checkState;

    /**
     * 回复人
     */
    private String answerName;

    /**
     * 回复时间
     */
    private LocalDateTime answerTime;

    /**
     * 回复内容
     */
    private String answerContent;

    /**
     * 是否需要审核(0=不需要 1=需要)
     */
    private Integer checkFlag;

    /**
     * 排序的值，如果需要置顶就直接99999
     */
    private Integer sort;

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

    private static final long serialVersionUID = 1L;

    public QuestionFeedback create(Long sId, Long dId, String name, String identity, String unit, String email,
                                   String phone, Long typeId, String ip, String feedbackTitle, LocalDateTime feedbackTime,
                                   String feedbackContent, Integer replyState, Integer checkState, String answerName,
                                   LocalDateTime answerTime, String answerContent, Integer checkFlag, Integer sort,
                                   Long createdBy, LocalDateTime createdTime) {
        this.sId = sId;
        this.dId = dId;
        this.name = name;
        this.identity = identity;
        this.unit = unit;
        this.email = email;
        this.phone = phone;
        this.typeId = typeId;
        this.ip = ip;
        this.feedbackTitle = feedbackTitle;
        this.feedbackTime = feedbackTime;
        this.feedbackContent = feedbackContent;
        this.replyState = replyState;
        this.checkState = checkState;
        this.answerName = answerName;
        this.answerTime = answerTime;
        this.answerContent = answerContent;
        this.checkFlag = checkFlag;
        this.sort = sort;
        this.createdBy = createdBy;
        this.createdTime = createdTime;

        return this;
    }

}