package com.kcidea.erms.enums.subject;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
public enum EnumSubjectLevel {

    /**
     * 学科门类(
     */
    学科门类(0,"学科门类"),
    /**
     * 一级学科
     */
    一级学科(1,"一级学科"),
    /**
     * 二级学科
     */
    二级学科(2,"二级学科");

    int value;
    String name;

    EnumSubjectLevel(int value, String name) {
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
    public static EnumSubjectLevel getSubjectLevel(int value) {
        for (EnumSubjectLevel enumSubjectLevel : EnumSubjectLevel.values()) {
            if (enumSubjectLevel.value == value) {
                return enumSubjectLevel;
            }
        }
        return EnumSubjectLevel.学科门类;
    }

}
