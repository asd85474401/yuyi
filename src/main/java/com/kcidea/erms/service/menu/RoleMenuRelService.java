package com.kcidea.erms.service.menu;

import com.kcidea.erms.model.menu.MenuModel;
import com.kcidea.erms.model.menu.RoleMenuRelModel;

import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/12
 **/
public interface RoleMenuRelService {
    /**
     * 查询所有菜单和角色对这些菜单的操作权限
     *
     * @param roleId 角色id
     * @return 菜单和角色操作权限集合
     * @author yeweiwei
     * @date 2021/11/12 18:11
     */
    List<MenuModel> findMenuListAndRoleRight(Long roleId);

    /**
     * 更新角色权限
     *
     * @param roleMenuRelModel 角色权限
     * @param userId           操作人id
     * @return 更新结果
     * @author yeweiwei
     * @date 2021/11/12 19:07
     */
    String updateRoleMenuRel(RoleMenuRelModel roleMenuRelModel, Long userId);
}
