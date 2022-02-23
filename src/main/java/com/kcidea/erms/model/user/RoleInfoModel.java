package com.kcidea.erms.model.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/12
 **/
@Data
public class RoleInfoModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private Long id;

    /**
     * 角色名称 必填项，不可重复，长度50个字符以内
     */
    @NotBlank(message = "角色名称不能为空！")
    @Size(max = 50, message = "角色名称不能超过50个字符！")
    private String name;

    /**
     * 备注说明
     */
    @Size(max = 150, message = "描述不能超过150个字符！")
    private String remark;

    /**
     * 用户数量
     */
    private Integer userCount;

    /**
     * 能否操作
     */
    private boolean operationFlag;
}
