package com.kcidea.erms.enums.database;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/12/7
 **/
public enum EnumDownloadType {

    /**
     * 在线查询
     */
    在线查询(0, "在线查询"),

    /**
     * 公司提供
     */
    公司提供(1, "公司提供"),

    ;

    final int value;
    final String name;

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    EnumDownloadType(int value, String name) {
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
    public static EnumDownloadType getEnumDownloadType(int value) {
        for (EnumDownloadType enumDownloadType : EnumDownloadType.values()) {
            if (enumDownloadType.getValue() == value) {
                return enumDownloadType;
            }
        }
        return null;
    }

}
