package com.kcidea.erms.enums.database;

import com.google.common.base.Strings;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
public enum EnumLanguageType {

    /**
     * 中文
     */
    中文(4L, "中文"),

    /**
     * 外文
     */
    外文(5L, "外文"),

    /**
     * 全部
     */
    全部(999L, "全部");

    final Long value;
    final String name;

    EnumLanguageType(Long value, String name) {
        this.value = value;
        this.name = name;
    }

    public Long getValue() {
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
    public static EnumLanguageType getLanguageType(Long value) {
        for (EnumLanguageType enumLanguageType : EnumLanguageType.values()) {
            if (enumLanguageType.value.equals(value)) {
                return enumLanguageType;
            }
        }
        return null;
    }

    /**
     * 根据名称获取对应的值
     *
     * @param name 传入名称
     * @return 值
     */
    public static Long getLanguageId(String name) {
        if (Strings.isNullOrEmpty(name)) {
            return null;
        }
        name = name.trim();
        for (EnumLanguageType enumLanguageType : EnumLanguageType.values()) {
            if (enumLanguageType.name.equals(name)) {
                return enumLanguageType.value;
            }
        }
        return null;
    }

}
