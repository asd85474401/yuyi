package com.kcidea.erms.enums.database;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/01/11
 **/
public enum EnumDataBaseType {

    /**
     * 子库
     */
    子库(0, "子库"),
    /**
     * 总库
     */
    总库(1, "总库"),
    /**
     * 分包
     */
    分包(2, "分包");

    int value;
    String name;

    EnumDataBaseType(int value, String name) {
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
    public static EnumDataBaseType getDatabaseType(int value) {
        for (EnumDataBaseType enumDataBaseType : EnumDataBaseType.values()) {
            if (enumDataBaseType.value == value) {
                return enumDataBaseType;
            }
        }
        return EnumDataBaseType.总库;
    }
}
