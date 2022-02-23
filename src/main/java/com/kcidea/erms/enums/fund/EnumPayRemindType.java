package com.kcidea.erms.enums.fund;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/17
 **/
public enum EnumPayRemindType {
    /**
     * 邮箱
     */
    邮箱(0, "邮箱"),

    /**
     * 站内信
     */
    站内信(1, "站内信");

    final int value;
    final String name;

    EnumPayRemindType(int value, String name) {
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
     * 根据类型的值获取对应的类型
     *
     * @param type 类型
     * @return 对应的枚举
     */
    public static EnumPayRemindType getEnumPayRemindType(int type) {
        for (EnumPayRemindType enumPayRemindType : EnumPayRemindType.values()) {
            if (type == enumPayRemindType.value) {
                return enumPayRemindType;
            }
        }
        return null;
    }
}
