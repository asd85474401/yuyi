package com.kcidea.erms.enums.database;


import com.kcidea.erms.common.util.ConvertUtil;

/**
 * @Description
 * @Author wuliwei
 * @Date 2020/6/8$
 **/
public enum EnumDataBaseProperty {

    /**
     * 电子期刊
     */
    期刊(6, "期刊"),
    /**
     * 电子图书
     */
    图书(7, "图书"),
    /**
     * 文摘事实
     */
    文摘事实(8, "文摘事实"),
    /**
     * 会议论文
     */
    会议论文(35, "会议论文"),
    /**
     * 学位论文
     */
    学位论文(36, "学位论文"),
    /**
     * 多媒体
     */
    多媒体(37, "多媒体"),
    /**
     * 专利
     */
    专利(38, "专利"),
    /**
     * 报纸
     */
    报纸(39, "报纸"),
    /**
     * 参考工具
     */
    参考工具(40, "参考工具"),
    /**
     * 其他
     */
    其他(41, "其他"),
    /**
     * 文摘引文
     */
    文摘引文(42, "文摘引文"),
    /**
     * 标准
     */
    标准(43, "标准"),
    /**
     * 标准
     */
    数据(50, "数据");


    int value;
    String name;

    EnumDataBaseProperty(int value, String name) {
        this.value = value;
        this.name = name;
    }



    public int getValue() {
        return value;
    }

    public Long getLongValue() {
        return ConvertUtil.objToLong(value);
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
    public static EnumDataBaseProperty getEnumDataBaseProperty(Long type) {
        for (EnumDataBaseProperty enumDataBaseProperty : EnumDataBaseProperty.values()) {
            if (enumDataBaseProperty.value == type.intValue()) {
                return enumDataBaseProperty;
            }
        }
        return null;
    }
}
