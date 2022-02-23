package com.kcidea.erms.enums.message;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/17
 **/
public enum EnumMessageType {
    /**
     * 支付到期提醒
     */
    支付到期提醒(1, "支付到期提醒");

    final int value;

    final String name;

    EnumMessageType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
