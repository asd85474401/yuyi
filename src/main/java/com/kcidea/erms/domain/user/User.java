package com.kcidea.erms.domain.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜单
 *
 * @author yeweiwei
 * @version 1.0
 * @date 2021/5/26
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 学校id
     */
    private Long sId;

    /**
     * 帐号
     */
    private String accountName;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称（姓名）
     */
    private String nickName;

    /**
     * 角色id
     */
    private Long roleId;

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
     * 登录次数
     */
    private Integer loginCount;

    /**
     * 最后一次登录时间
     */
    private LocalDateTime lastLoginTime;

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
}
