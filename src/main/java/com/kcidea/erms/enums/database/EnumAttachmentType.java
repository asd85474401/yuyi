package com.kcidea.erms.enums.database;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/24
 **/
public enum EnumAttachmentType {

    /**
     * 评估附件
     */
    评估附件(1, "评估附件"),

    /**
     * 停订附件
     */
    停订附件(2, "停订附件"),

    /**
     * 使用指南
     */
    使用指南(3, "使用指南"),

    /**
     * 订购列表
     */
    订购列表(4, "订购列表"),
    
    /**
     * Counter报告
     */
    Counter报告(5, "Counter报告"),

    ;

    final int value;
    final String name;

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }


    EnumAttachmentType(int value, String name) {
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
    public static EnumAttachmentType getEnumAttachmentType(int value) {
        for (EnumAttachmentType enumAttachmentType : EnumAttachmentType.values()) {
            if (enumAttachmentType.getValue() == value) {
                return enumAttachmentType;
            }
        }
        return null;
    }
}
