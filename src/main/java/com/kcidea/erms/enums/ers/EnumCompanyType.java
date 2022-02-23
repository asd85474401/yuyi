package com.kcidea.erms.enums.ers;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/12/02
 **/
public enum EnumCompanyType {

    /**
     * 系统默认
     */
    系统默认(1, "系统默认"),

    /**
     * 学校自定义
     */
    自定义(2, "自定义"),;

    private final int value;

    private final String name;

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    EnumCompanyType(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
