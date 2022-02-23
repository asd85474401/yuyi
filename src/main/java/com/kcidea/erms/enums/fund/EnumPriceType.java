package com.kcidea.erms.enums.fund;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
public enum EnumPriceType {

    /**
     * 人民币
     */
    人民币(1, "CNY"),

    /**
     * 美元
     */
    美元(2, "USD"),

    /**
     * 欧元
     */
    欧元(3, "EUR"),

    /**
     * 英镑
     */
    英镑(4, "GBP"),

    /**
     * 日元
     */
    日元(5, "JPY"),

    /**
     * 韩元
     */
    韩元(6, "KRW"),

    /**
     * 港元
     */
    港元(7, "HKD"),

    /**
     * 澳元
     */
    澳元(8, "AUD"),

    /**
     * 新台币
     */
    新台币(9, "TWD");

    final int value;
    final String name;

    EnumPriceType(int value, String name) {
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
