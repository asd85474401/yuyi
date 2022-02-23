package com.kcidea.erms.enums.database;

import com.google.common.base.Strings;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/12/7
 **/
public enum EnumPaperType {

    /**
     * 全部
     */
    电子(0, "电子"),

    /**
     * 外文期刊
     */
    纸本(1, "纸本"),

    /**
     * 外文图书
     */
    全部(2, "全部");

    int value;
    String name;

    EnumPaperType(int value, String name) {
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
     * @param type 传入的值
     * @return 枚举
     */
    public static EnumPaperType getPaperType(Integer type) {
        for (EnumPaperType enumPaperType : EnumPaperType.values()) {
            if (enumPaperType.value == type) {
                return enumPaperType;
            }
        }
        return null;
    }

    /**
     * 根据名称获取对应的值
     *
     * @param name 传入的名称
     * @return 枚举
     */
    public static Integer getPaperType(String name) {
        if (Strings.isNullOrEmpty(name)) {
            return null;
        }
        name = name.trim();
        for (EnumPaperType enumPaperType : EnumPaperType.values()) {
            if (enumPaperType.name.equals(name)) {
                return enumPaperType.getValue();
            }
        }
        return null;
    }

}
