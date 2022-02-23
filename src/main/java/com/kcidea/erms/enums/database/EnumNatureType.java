package com.kcidea.erms.enums.database;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/12/7
 **/
public enum EnumNatureType {

    /**
     * 自建数据库
     */
    自建数据库(0, "自建数据库"),

    /**
     * 商购数据库
     */
    商购数据库(1, "商购数据库"),

    /**
     * OA数据库
     */
    OA数据库(2, "OA数据库"),

    /**
     * 试用数据库
     */
    试用数据库(3, "试用数据库"),

    ;

    final int value;
    final String name;

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    EnumNatureType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 根据值获取枚举
     *
     * @param value 值
     * @return 枚举
     * @author yeweiwei
     * @date 2021/12/8 11:00
     */
    public static EnumNatureType getEnumNatureType(int value) {
        for (EnumNatureType enumNatureType : EnumNatureType.values()) {
            if (enumNatureType.getValue() == value) {
                return enumNatureType;
            }
        }
        return null;
    }

    /**
     * 获取系统默认的数据库性质
     *
     * @return 默认地区的列表
     * @author huxubin
     * @date 2022/1/6 14:39
     */
    public static List<String> getTypeList() {
        List<String> typeList = Lists.newArrayList();
        for (EnumNatureType enumNatureType : EnumNatureType.values()) {
            typeList.add(enumNatureType.getName());
        }
        return typeList;
    }

}
