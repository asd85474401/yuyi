package com.kcidea.erms.model.fund;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/17
 **/
@Data
public class PayRemindInfoModel  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 距离截止多少天提醒
     */
    @NotNull(message = "请填写付款到期提醒时间！")
    @Range(min = 1, max = 999, message = "付款到期提醒时间必须是3位以内的正整数！")
    private Integer remindDay;

    /**
     * 提醒方式（0=邮件 1=站内信）
     */
    @NotNull(message = "请选择提醒方式！")
    private Integer type;

    /**
     * 提醒的用户id集合
     */
    @NotEmpty(message = "请至少选择一位提醒的用户！")
    private List<Long> userIdList;
}
