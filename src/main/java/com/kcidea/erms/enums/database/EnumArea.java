package com.kcidea.erms.enums.database;

import com.google.common.collect.Lists;
import com.kcidea.erms.enums.menu.EnumMenu;

import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2022/1/5
 **/
public enum EnumArea {

    /**
     * 大陆
     */
    大陆("大陆", 1),

    /**
     * 港澳台
     */
    港澳台("港澳台", 2),

    /**
     * 国际
     */
    国际("国际", 3);

    //排序字段
    final int sort;
    //名称
    final String name;

    EnumArea(String name, int sort) {
        this.name = name;
        this.sort = sort;
    }

    public int getSort() {
        return sort;
    }

    public String getName() {
        return name;
    }

    /**
     * 获取系统默认的地区
     *
     * @return 默认地区的列表
     * @author huxubin
     * @date 2022/1/6 14:39
     */
    public static List<String> getAreaList() {
        List<String> areaList = Lists.newArrayList();
        for (EnumArea enumArea : EnumArea.values()) {
            areaList.add(enumArea.getName());
        }
        return areaList;
    }
}
