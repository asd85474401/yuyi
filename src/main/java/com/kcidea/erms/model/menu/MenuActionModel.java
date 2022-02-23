package com.kcidea.erms.model.menu;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/12
 **/
@Data
public class MenuActionModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单id
     */
    @NotNull(message = "权限菜单不能为空！")
    private Long menuId;

    /**
     * 权限集合
     */
    private List<String> roleRightsList;
}
