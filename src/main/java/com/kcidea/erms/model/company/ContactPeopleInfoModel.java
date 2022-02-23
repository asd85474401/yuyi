package com.kcidea.erms.model.company;

import com.kcidea.erms.common.constant.Vm;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/5 13:40
 **/
@Data
public class ContactPeopleInfoModel implements Serializable {

    /**
     * 联系人id
     */
    private Long contactId;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 数据库id
     */
    private Long dId;

    /**
     * apiKey
     */
    private String apiKey;

    /**
     * 联系人姓名
     */
    @NotBlank(message = "请输入联系人姓名")
    @Length(max = 150, message = "联系人姓名最大长度为{max}个字符")
    private String name;

    /**
     * 手机号
     */
    @NotBlank(message = "请输入手机号")
    @Pattern(regexp = "^$|^(13[0-9]|14[01456879]|15[0-3,5-9]|16[2567]|17[0-8]|18[0-9]|19[0-3,5-9])\\d{8}$", message = Vm.MOBILE_FIT_ERROR)
    private String phone;

    /**
     * 联系邮箱
     */
    @Email
    private String email;

    /**
     * 职位
     */
    @NotBlank(message = "请选择职位类型")
    private String position;

    /**
     * 职责描述
     */
    @Length(max = 150, message = "职责描述最大长度为{max}个字符")
    private String remark;

    /**
     * 座机号
     */
    @Length(max = 20, message = "座机号最大长度为{max}个字符")
    private String telephone;

    /**
     * 联系qq
     */
    @Length(max = 100, message = "QQ最大长度为{max}个字符")
    private String qq;

    /**
     * 微信号
     */
    @Length(max = 100, message = "微信最大长度为{max}个字符")
    private String wechat;

    /**
     * 邮寄地址
     */
    @Length(max = 150, message = "邮寄地址最大长度为{max}个字符")
    private String address;

    /**
     * 联系人类型 , 0=供应商联系人 1=代理商联系人
     */
    private Integer contactType;

    private static final long serialVersionUID = 1L;
}