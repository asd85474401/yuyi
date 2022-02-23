package com.kcidea.erms.model.feedback;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/22
 **/
@Data
public class FeedbackModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 反馈标题
     */
    private String feedbackTitle;

    /**
     * 反馈内容
     */
    private String feedbackContent;

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
     * 反馈时间
     */
    private LocalDateTime feedbackTime;

    /**
     * 类型
     */
    private Long typeId;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 数据库id
     */
    private Long did;

    /**
     * 数据库名称
     */
    private String dName;

    /**
     * 审核状态(0=待审核 1=审核通过  2=审核未通过)
     */
    private Integer checkState;

    /**
     * 审核状态名称
     */
    private String checkStateName;

    /**
     * 审核说明
     */
    private String remark;

}
