package com.kcidea.erms.service.menu;

import com.kcidea.erms.model.menu.MenuModel;

import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
public interface MenuService {

    /**
     * 根据角色id查询对应的菜单权限
     *
     * @param roleId 角色id
     * @return 菜单权限
     * @author huxubin
     * @date 2021/11/10 9:43
     */
    List<MenuModel> findMenuListByRoleId(Long roleId);
}
