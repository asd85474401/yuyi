package com.kcidea.erms.model.company;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/5 13:40
 **/
@Data
public class AccessUrlInfoModel implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 访问id
     */
    private Long accessId;

    /**
     * apiKey
     */
    private String apiKey;

    /**
     * 数据库id
     */
    private Long dId;

    /**
     * 总库id
     */
    private Long totalDid;

    /**
     * 数据库名称
     */
    private String name;

    /**
     * 访问类型
     */
    @NotBlank(message = "请选择访问类型")
    @Length(max = 100, message = "访问类型最大长度为{max}个字符")
    private String accessType;

    /**
     * 登录方式
     */
    @NotBlank(message = "请选择登录方式")
    @Length(max = 300, message = "登录方式最大长度为{max}个字符")
    private String loginType;

    /**
     * 访问链接
     */
    @NotBlank(message = "请填写访问链接")
    @Length(max = 500, message = "访问链接最大长度为{max}个字符")
    @Pattern(regexp = "^(http[s]?://).*$", message = "请输入格式正确的访问链接")
    private String url;

    /**
     * 访问提示
     */
    @Length(max = 500, message = "访问提示最大长度为{max}个字符")
    private String accessTips;

    /**
     * 备注说明
     */
    @Length(max = 500, message = "备注说明最大长度为{max}个字符")
    private String remark;

    private static final long serialVersionUID = 1L;
}