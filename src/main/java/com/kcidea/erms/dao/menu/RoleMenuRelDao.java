package com.kcidea.erms.dao.menu;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.menu.RoleMenuRel;
import com.kcidea.erms.model.menu.MenuModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
public interface RoleMenuRelDao extends BaseMapper<RoleMenuRel> {

    /**
     * 根据角色的id查询对应的权限
     *
     * @param roleId 角色id
     * @return 权限
     * @author huxubin
     * @date 2021/11/10 17:49
     */
    List<MenuModel> findMenuModelListByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色id删除权限
     *
     * @param roleId 角色id
     * @author yeweiwei
     * @date 2021/11/12 19:17
     */
    void deleteByRoleId(@Param("roleId") Long roleId);
}
