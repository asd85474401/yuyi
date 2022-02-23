package com.kcidea.erms.enums.database;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/01/05
 **/
public enum EnumTableType {

    /**
     * 基本信息
     */
    基本信息(1, "基本信息"),


    /**
     * 库商信息
     */
    库商信息(2, "库商信息"),


    /**
     * 访问信息
     */
    访问信息(3, "访问信息"),

    ;

    final int value;
    final String name;

    public int getValue() {
        return value;
    }

    public long getLongValue() {
        return (long)value;
    }

    public String getName() {
        return name;
    }

    EnumTableType(int value, String name) {
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
    public static EnumTableType getEnumTableType(int value) {
        for (EnumTableType enumTableType : EnumTableType.values()) {
            if (enumTableType.getValue() == value) {
                return enumTableType;
            }
        }
        return EnumTableType.基本信息;
    }
}
