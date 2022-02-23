package com.kcidea.erms.model.feedback;

import com.kcidea.erms.common.constant.Vm;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/22
 **/
@Data
public class QuestionFeedbackInfoModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据库id
     */
    private Long did;

    /**
     * 反馈标题
     */
    @NotBlank(message = "标题不能为空")
    private String feedbackTitle;

    /**
     * 用户姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String name;

    /**
     * 类型
     */
    @NotNull(message = "反馈类型不能为空")
    private Long typeId;

    /**
     * 用户邮箱
     */
    @Email(message = Vm.EMAIL_FIT_ERROR)
    private String email;

    /**
     * 联系电话
     */
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-3,5-9]|16[2567]|17[0-8]|18[0-9]|19[0-3,5-9])\\d{8}$", message = Vm.MOBILE_FIT_ERROR)
    private String phone;

    /**
     * 反馈内容
     */
    @NotBlank(message = "反馈内容不能为空")
    @Size(max = 1000, message = "反馈内容不能超过1000个字符")
    private String feedbackContent;

    /**
     * 反馈时间
     */
    @NotNull
    private LocalDateTime feedbackTime;

    /**
     * 反馈人的IP
     */
    @NotNull
    private String ip;

    /**
     * 回复状态（0=未回复 1=已回复）
     */
    @NotNull
    @Range(min = 0, max = 1, message = Vm.ERROR_PARAMS)
    private Integer replyState;

    /**
     * 审核状态(0=待审核 1=审核通过  2=审核未通过)
     */
    @NotNull
    @Range(min = 0, max = 2, message = Vm.ERROR_PARAMS)
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
    @NotNull(message = "存档规则为必填项，请选择存档规则")
    @Range(min = 0, max = 2, message = Vm.ERROR_PARAMS)
    private Integer checkFlag;

    /**
     * 排序的值，如果需要置顶就直接99999
     */
    private Integer sort;


}
