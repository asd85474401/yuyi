package com.kcidea.erms.enums.common;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
public enum EnumTrueFalse {

    /**
     * 是
     */
    是(1, "是"),
    /**
     * counter配置
     */
    否(0, "否");

    final int value;
    final String name;

    EnumTrueFalse(int value, String name) {
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
    public static EnumTrueFalse getTrueFalse(int value) {
        for (EnumTrueFalse enumTrueFalse : EnumTrueFalse.values()) {
            if (enumTrueFalse.value == value) {
                return enumTrueFalse;
            }
        }
        return null;
    }

    /**
     * 根据名称获取对应的枚举
     *
     * @param name 传入的名称
     * @return 枚举
     */
    public static EnumTrueFalse getTrueFalse(String name) {
        for (EnumTrueFalse enumTrueFalse : EnumTrueFalse.values()) {
            if (enumTrueFalse.name.equals(name)) {
                return enumTrueFalse;
            }
        }
        return null;
    }

}
