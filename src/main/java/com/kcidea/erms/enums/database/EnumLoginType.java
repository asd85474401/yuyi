package com.kcidea.erms.enums.database;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
public enum EnumLoginType {

    /**
     * VPN登录
     */
    VPN登录(1, "VPN登录"),

    /**
     * 校内IP范围访问
     */
    校内IP范围访问(2, "校内IP范围访问"),

    /**
     * 帐号密码登录
     */
    帐号密码登录(3, "帐号密码登录"),

    /**
     * 全部
     */
    全部(999, "全部");

    final int value;
    final String name;

    EnumLoginType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据值获取对应的枚举
     *
     * @param value 传入的值
     * @return 枚举
     */
    public static EnumLoginType getEnumLoginType(int value) {
        for (EnumLoginType enumLoginType : EnumLoginType.values()) {
            if (enumLoginType.getValue() == value) {
                return enumLoginType;
            }
        }
        return null;
    }

    /**
     * 根据名称获得枚举的值
     *
     * @param name 名称
     * @return 枚举值
     * @author yeweiwei
     * @date 2021/11/30 10:23
     */
    public static Integer getLoginType(String name) {
        if (Strings.isNullOrEmpty(name)) {
            return null;
        }
        name = name.trim();
        for (EnumLoginType enumLoginType : EnumLoginType.values()) {
            if (name.equals(enumLoginType.getName())) {
                return enumLoginType.getValue();
            }
        }
        return null;
    }

    /**
     * 获取枚举所有的name值（除了全部）
     *
     * @return 枚举name集合
     * @author yeweiwei
     * @date 2021/12/3 9:05
     */
    public static Set<String> getNameSet() {
        Set<String> set = Sets.newHashSet();
        for (EnumLoginType enumLoginType : EnumLoginType.values()) {
            if (!"全部".equals(enumLoginType.getName())) {
                set.add(enumLoginType.getName());
            }
        }

        return set;
    }
}
