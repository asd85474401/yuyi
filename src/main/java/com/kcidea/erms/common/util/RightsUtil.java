package com.kcidea.erms.common.util;

import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.menu.MenuModel;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/24
 **/
public class RightsUtil {

    /**
     * 判断该用户是否拥有需要的菜单和该菜单的权限
     *
     * @param menuList       用户拥有的菜单权限集合
     * @param menuStr        需要的菜单
     * @param enumUserAction 该菜单需要的权限
     * @return boolean
     * @author yeweiwei
     * @date 2021/5/27 10:14
     */
    public static Boolean checkAdminMenu(List<MenuModel> menuList, String menuStr, EnumUserAction enumUserAction) {
        if (!CollectionUtils.isEmpty(menuList)) {
            for (MenuModel menuModel : menuList) {
                //判断当前菜单权限是否为需要的权限
                if (menuModel.getModelName().equals(menuStr)) {
                    return checkUserMenuRight(menuModel, enumUserAction);
                }
                //如果不是，判断其子菜单是否为需要的权限
                List<MenuModel> subMenuList = menuModel.getSubMenu();
                if (checkAdminMenu(subMenuList, menuStr, enumUserAction)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断用户对菜单的操作权限是否有需要的权限
     *
     * @param menuModel      用户对该菜单拥有的权限
     * @param enumUserAction 该菜单需要的权限
     * @return boolean
     */
    public static Boolean checkUserMenuRight(MenuModel menuModel, EnumUserAction enumUserAction) {
        int rightFlag;
        switch (enumUserAction) {
            case 查询: {
                rightFlag = menuModel.getSelectFlag();
                break;
            }
            case 新增: {
                rightFlag = menuModel.getInsertFlag();
                break;
            }
            case 修改: {
                rightFlag = menuModel.getUpdateFlag();
                break;
            }
            case 删除: {
                rightFlag = menuModel.getDeleteFlag();
                break;
            }
            case 导入: {
                rightFlag = menuModel.getImportFlag();
                break;
            }
            case 导出: {
                rightFlag = menuModel.getExportFlag();
                break;
            }
            case 其他: {
                rightFlag = menuModel.getOtherFlag();
                break;
            }
            case 显示:
            default: {
                rightFlag = menuModel.getShowFlag();
                break;
            }
        }

        return rightFlag == EnumTrueFalse.是.getValue();
    }
}
