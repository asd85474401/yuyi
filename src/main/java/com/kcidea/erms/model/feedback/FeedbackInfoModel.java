package com.kcidea.erms.model.feedback;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kcidea.erms.common.constant.Vm;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/22
 **/
@Data
public class FeedbackInfoModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 反馈标题
     */
    @NotBlank(message = Vm.FIELD_NOT_BLANK)
    @Length(max = 150, message = "反馈标题不能超过150个字符")
    private String feedbackTitle;

    /**
     * 用户姓名
     */
    @NotBlank(message = Vm.FIELD_NOT_BLANK)
    @Length(max = 100, message = "反馈人不能超过100个字符")
    private String name;

    /**
     * 用户身份
     */
    @Length(max = 100, message = "用户身份不能超过100个字符")
    private String identity;

    /**
     * 用户单位
     */
    @Length(max = 150, message = "用户单位不能超过150个字符")
    private String unit;

    /**
     * 反馈时间
     */
    @NotNull(message = Vm.FIELD_NOT_BLANK)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat("yyyy-MM-dd HH:mm")
    private LocalDateTime feedbackTime;

    /**
     * 用户邮箱
     */
    @Email(message = Vm.EMAIL_FIT_ERROR)
    @Length(max = 150, message = "用户邮箱不能超过150个字符")
    private String email;

    /**
     * 联系电话
     */
    @Pattern(regexp = "^$|^(13[0-9]|14[01456879]|15[0-3,5-9]|16[2567]|17[0-8]|18[0-9]|19[0-3,5-9])\\d{8}$", message = Vm.MOBILE_FIT_ERROR)
    private String phone;

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
    private Long dId;

    /**
     * 数据库名称
     */
    private String databaseName;

    /**
     * 反馈内容
     */
    @NotBlank(message = Vm.FIELD_NOT_BLANK)
    @Size(max = 1000, message = "反馈内容不能超过1000个字符")
    private String feedbackContent;

    /**
     * 回复人
     */
    @NotBlank
    @Length(max = 100, message = "回复人不能超过100个字符")
    private String answerName;

    /**
     * 回复时间
     */
    @NotNull(message = Vm.FIELD_NOT_BLANK)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat("yyyy-MM-dd HH:mm")
    private LocalDateTime answerTime;

    /**
     * 回复内容
     */
    @NotBlank(message = Vm.FIELD_NOT_BLANK)
    @Size(max = 1000, message = "回复内容不能超过1000个字符")
    private String answerContent;

    /**
     * 是否需要审核(0=不需要 1=需要)
     * 存档规则: 0=已审核通过，直接存档；1=未审核，审核后存档
     */
    @NotNull
    @Range(min = 0, max = 1, message = Vm.ERROR_PARAMS)
    private Integer checkFlag;

    /**
     * 是否重新提交审核（1=重新提交审核，0=不需要提交审核）
     */
    private Integer recheckFlag;

    public FeedbackInfoModel create(String feedbackTitle, String name, String identity, String unit, LocalDateTime feedbackTime, String email,
                                    String phone, Long typeId, Long dId, String feedbackContent, String answerName,
                                    LocalDateTime answerTime, String answerContent, Integer checkFlag) {
        this.feedbackTitle = feedbackTitle;
        this.name = name;
        this.identity = identity;
        this.unit = unit;
        this.feedbackTime = feedbackTime;
        this.email = email;
        this.phone = phone;
        this.typeId = typeId;
        this.dId = dId;
        this.feedbackContent = feedbackContent;
        this.answerName = answerName;
        this.answerTime = answerTime;
        this.answerContent = answerContent;
        this.checkFlag = checkFlag;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FeedbackInfoModel that = (FeedbackInfoModel) o;
        return Objects.equals(feedbackTitle, that.feedbackTitle) &&
                Objects.equals(name, that.name) &&
                Objects.equals(identity, that.identity) &&
                Objects.equals(unit, that.unit) &&
                Objects.equals(feedbackTime, that.feedbackTime) &&
                Objects.equals(email, that.email) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(typeId, that.typeId) &&
                Objects.equals(typeName, that.typeName) &&
                Objects.equals(dId, that.dId) &&
                Objects.equals(databaseName, that.databaseName) &&
                Objects.equals(feedbackContent, that.feedbackContent) &&
                Objects.equals(answerName, that.answerName) &&
                Objects.equals(answerTime, that.answerTime) &&
                Objects.equals(answerContent, that.answerContent) &&
                Objects.equals(checkFlag, that.checkFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackTitle, name, feedbackTime, email, phone, typeId, typeName, dId, databaseName,
                feedbackContent, answerName, answerTime, answerContent, checkFlag);
    }
}
