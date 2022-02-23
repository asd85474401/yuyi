package com.kcidea.erms.service.menu.impl;

import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.util.RedisUtil;
import com.kcidea.erms.common.util.ReflexUtil;
import com.kcidea.erms.dao.menu.MenuDao;
import com.kcidea.erms.dao.menu.RoleMenuRelDao;
import com.kcidea.erms.domain.menu.RoleMenuRel;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.menu.MenuActionModel;
import com.kcidea.erms.model.menu.MenuModel;
import com.kcidea.erms.model.menu.RoleMenuRelModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.menu.RoleMenuRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/12
 **/
@Slf4j
@Service
public class RoleMenuRelServiceImpl extends BaseService implements RoleMenuRelService {

    @Resource
    private MenuDao menuDao;

    @Resource
    private RoleMenuRelDao roleMenuRelDao;

    /**
     * 查询所有菜单和角色对这些菜单的操作权限
     *
     * @param roleId 角色id
     * @return 菜单和角色操作权限集合
     * @author yeweiwei
     * @date 2021/11/12 18:11
     */
    @Override
    public List<MenuModel> findMenuListAndRoleRight(Long roleId) {
        //查询所有菜单
        List<MenuModel> allMenu = menuDao.findAllMenu();

        //角色的菜单权限
        List<MenuModel> roleMenuModels = roleMenuRelDao.findMenuModelListByRoleId(roleId);

        for (MenuModel menu : allMenu) {
            menu.setRightList(MenuModel.getRightList(menu));
            MenuModel roleMenu = roleMenuModels.stream()
                    .filter(m -> menu.getId().equals(m.getId())).findFirst().orElse(null);
            menu.setRoleRightsList(null != roleMenu ? MenuModel.getRightList(roleMenu) : Collections.emptyList());
        }
        return MenuModel.buildMenuTree(allMenu, 0L);
    }

    /**
     * 更新角色权限
     *
     * @param roleMenuRelModel 角色权限
     * @param userId           操作人id
     * @return 更新结果
     * @author yeweiwei
     * @date 2021/11/12 19:08
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateRoleMenuRel(RoleMenuRelModel roleMenuRelModel, Long userId) {
        Long roleId = roleMenuRelModel.getRoleId();
        List<MenuActionModel> menuActionList = roleMenuRelModel.getMenuActionList();

        //删除角色原来的权限
        roleMenuRelDao.deleteByRoleId(roleId);

        //新增角色权限
        LocalDateTime now = LocalDateTime.now();
        if (!CollectionUtils.isEmpty(menuActionList)){
            for (MenuActionModel menuActionModel : menuActionList) {
                List<String> rightList = menuActionModel.getRoleRightsList();
                if (CollectionUtils.isEmpty(rightList)){
                    continue;
                }
                RoleMenuRel relModel = new RoleMenuRel();
                for (String rightName : rightList) {
                    EnumUserAction enumUserAction = EnumUserAction.valueOf(rightName);
                    ReflexUtil.setValue(relModel, enumUserAction.getOperationFlag(), EnumTrueFalse.是.getValue());
                }
                relModel.setRoleId(roleId);
                relModel.setMenuId(menuActionModel.getMenuId());
                relModel.setCreatedBy(userId);
                relModel.setCreatedTime(now);
                roleMenuRelDao.insert(relModel);
            }
        }

        //删除该角色的权限缓存
        RedisUtil.deleteByKey(Constant.RedisKey.ROLE_MENU_KEY.concat(Long.toString(roleId)));
        return Vm.UPDATE_SUCCESS;
    }
}
