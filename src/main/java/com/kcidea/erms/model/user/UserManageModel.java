package com.kcidea.erms.model.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/12
 **/
@Data
@Accessors(chain = true)
public class UserManageModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    private Long id;

    /**
     * 帐号
     */
    private String accountName;

    /**
     * 昵称（姓名）
     */
    private String nickName;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 角色
     */
    private String role;

    /**
     * 禁用标识
     */
    private Integer disableFlag;

    /**
     * 部门
     */
    private String department;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 是否是本账号,1本账号、0不是本账号
     */
    private Integer myFlag;

}
