package com.kcidea.erms.model.menu;

import com.google.common.collect.Lists;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.user.EnumUserAction;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
@Data
public class MenuModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * modelName
     */
    private String modelName;

    /**
     * 菜单层级
     */
    private Integer level;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 父菜单id
     */
    private Long parentId;

    /**
     * 父菜单name
     */
    private String parentName;

    /**
     * 子级菜单
     */
    private List<MenuModel> subMenu;

    /**
     * 是否拥有子级菜单标识符，0=没有；1=有
     */
    private Integer subMenuFlag;

    /**
     * 显示操作
     */
    private Integer showFlag;

    /**
     * 查看操作
     */
    private Integer selectFlag;

    /**
     * 插入操作
     */
    private Integer insertFlag;

    /**
     * 更新操作
     */
    private Integer updateFlag;

    /**
     * 删除操作
     */
    private Integer deleteFlag;

    /**
     * 导入操作
     */
    private Integer importFlag;

    /**
     * 导出操作
     */
    private Integer exportFlag;

    /**
     * 其它操作
     */
    private Integer otherFlag;

    /**
     * 菜单拥有的功能集合
     */
    private List<String> rightList;

    /**
     * 角色拥有的菜单权限
     */
    private List<String> roleRightsList;

    /**
     * 处理菜单的rightList，有某个权限，集合中就添加对应权限的名称
     *
     * @param menuModel 后台菜单
     * @return 拥有的权限集合
     * @author yeweiwei
     * @date 2021/5/31 18:09
     */
    public static List<String> getRightList(MenuModel menuModel) {
        List<String> rightList = Lists.newArrayList();
        if (menuModel.getShowFlag() == EnumTrueFalse.是.getValue()) {
            rightList.add(EnumUserAction.显示.getName());
        }
        if (menuModel.getSelectFlag() == EnumTrueFalse.是.getValue()) {
            rightList.add(EnumUserAction.查询.getName());
        }
        if (menuModel.getInsertFlag() == EnumTrueFalse.是.getValue()) {
            rightList.add(EnumUserAction.新增.getName());
        }
        if (menuModel.getUpdateFlag() == EnumTrueFalse.是.getValue()) {
            rightList.add(EnumUserAction.修改.getName());
        }
        if (menuModel.getDeleteFlag() == EnumTrueFalse.是.getValue()) {
            rightList.add(EnumUserAction.删除.getName());
        }
        if (menuModel.getImportFlag() == EnumTrueFalse.是.getValue()) {
            rightList.add(EnumUserAction.导入.getName());
        }
        if (menuModel.getExportFlag() == EnumTrueFalse.是.getValue()) {
            rightList.add(EnumUserAction.导出.getName());
        }
        if (menuModel.getOtherFlag() == EnumTrueFalse.是.getValue()) {
            rightList.add(EnumUserAction.其他.getName());
        }

        return rightList;
    }

    /**
     * 构建树形结构
     *
     * @param list     查询出来的数据
     * @param parentId 父菜单的id
     * @return 构建出来的结果
     * @author huxubin
     * @date 2021/11/10 18:04
     */
    public static List<MenuModel> buildMenuTree(List<MenuModel> list, Long parentId) {
        List<MenuModel> menuList =
                list.stream().filter(s -> Objects.equals(s.getParentId(), parentId)).collect(Collectors.toList());
        for (MenuModel menuModel : menuList) {
            List<MenuModel> childMenuList = buildMenuTree(list, menuModel.getId());
            if (!CollectionUtils.isEmpty(childMenuList)){
                menuModel.setSubMenu(childMenuList);
                menuModel.setSubMenuFlag(EnumTrueFalse.是.getValue());
            }

        }
        return menuList;
    }
}
