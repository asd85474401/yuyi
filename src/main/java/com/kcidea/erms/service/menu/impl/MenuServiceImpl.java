package com.kcidea.erms.service.menu.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.util.RedisUtil;
import com.kcidea.erms.dao.menu.RoleMenuRelDao;
import com.kcidea.erms.model.menu.MenuModel;
import com.kcidea.erms.service.menu.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private RoleMenuRelDao roleMenuRelDao;

    /**
     * 根据角色id查询对应的菜单权限
     *
     * @param roleId 角色id
     * @return 菜单权限
     * @author huxubin
     * @date 2021/11/10 9:43
     */
    @Override
    public List<MenuModel> findMenuListByRoleId(Long roleId) {

        if (roleId == null) {
            return Lists.newArrayList();
        }

        //优先从redis中取数据
        String redisKey = Constant.RedisKey.ROLE_MENU_KEY.concat(Long.toString(roleId));
        String json = RedisUtil.getStringByKey(redisKey);

        List<MenuModel> list;

        //如果redis没有，再去查询
        if (Strings.isNullOrEmpty(json)) {
            list = roleMenuRelDao.findMenuModelListByRoleId(roleId);
            list.forEach(x -> x.setRoleRightsList(MenuModel.getRightList(x)));
            list = MenuModel.buildMenuTree(list, 0L);
            RedisUtil.setValueAndTime(redisKey, JSON.toJSONString(list), Constant.RedisTime.ONE_DAY);
        } else {
            list = JSON.parseArray(json, MenuModel.class);
        }
        return list;
    }

}
