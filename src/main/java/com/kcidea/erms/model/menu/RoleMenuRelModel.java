package com.kcidea.erms.model.menu;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/12
 **/
@Data
public class RoleMenuRelModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @NotNull(message = "角色不能为空！")
    private Long roleId;

    /**
     * 角色菜单权限集合
     */
    @Valid
    private List<MenuActionModel> menuActionList;
}
