package com.kcidea.erms.enums.database;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/12/7
 **/
public enum EnumBuyType {

    /**
     * DRAA采购
     */
    DRAA(0, "DRAA"),

    /**
     * 独立采购
     */
    独立采购(1, "独立采购"),

    /**
     * Balis联盟采购
     */
    Balis联盟采购(2, "Balis联盟采购"),

    /**
     * 院系采购
     */
    院系采购(3, "院系采购"),

    /**
     * 网图数据库
     */
    网图数据库(4, "网图数据库"),

    /**
     * 赠送
     */
    赠送(5, "赠送"),

    /**
     * 停订仍可用
     */
    停订仍可用(6, "停订仍可用");

    final int value;
    final String name;

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }


    EnumBuyType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 根据值获取枚举
     *
     * @param value 值
     * @return 枚举
     * @author yeweiwei
     * @date 2021/12/8 10:56
     */
    public static EnumBuyType getEnumBuyType(int value) {
        for (EnumBuyType enumBuyType : EnumBuyType.values()) {
            if (enumBuyType.getValue() == value) {
                return enumBuyType;
            }
        }
        return null;
    }

}
