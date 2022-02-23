package com.kcidea.erms.dao.menu;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.menu.Menu;
import com.kcidea.erms.model.menu.MenuModel;

import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
public interface MenuDao extends BaseMapper<Menu> {

    /**
     * 查询所有菜单
     *
     * @return 菜单集合
     * @author yeweiwei
     * @date 2021/11/12 18:14
     */
    List<MenuModel> findAllMenu();
}
