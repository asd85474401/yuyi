package com.kcidea.erms.enums.database;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/11 15:08
 **/
public enum EnumDataType {

    /**
     * 外文期刊
     */
    外文期刊(1, "外文期刊", EnumDataBaseProperty.期刊.getValue(), EnumLanguageType.外文.getValue()),
    /**
     * 外文图书
     */
    外文图书(2, "外文图书", EnumDataBaseProperty.图书.getValue(), EnumLanguageType.外文.getValue()),
    /**
     * 中文期刊
     */
    中文期刊(3, "中文期刊", EnumDataBaseProperty.期刊.getValue(), EnumLanguageType.中文.getValue()),
    /**
     * 中文图书
     */
    中文图书(4, "中文图书", EnumDataBaseProperty.图书.getValue(), EnumLanguageType.中文.getValue()),
    /**
     * PQDT论文
     */
    PQDT论文(5, "学位论文", 0, 0L),

    /**
     * 会议录
     */
    会议录(6, "会议录", 0, 0L),
    /**
     * 文摘
     */
    文摘(7, "文摘引文", 42, 0L),

    /**
     * 热点发文
     */
    热点发文(8, "热点发文", 0, 0L),
    /**
     * 专利
     */
    专利(9, "专利", 0, 0L),
    /**
     * 其他
     */
    其他(10, "其他", 0, 0L),


    /**
     * 全部
     */
    全部(999, "全部", 0, 0L);

    int value;
    String name;
    Integer property;
    Long language;

    EnumDataType(int value, String name, int property, Long language) {
        this.value = value;
        this.name = name;
        this.property = property;
        this.language = language;
    }

    /**
     * 格式化学科的数据类型
     *
     * @param enumDataType 数据类型
     * @return 格式化学科的数据类型
     * @author majuehao
     * @date 2022/1/11 15:12
     **/
    public static Integer formatSubjectDataType(EnumDataType enumDataType) {
        if (EnumDataType.文摘 == enumDataType) {
            return EnumDataType.外文期刊.getValue();
        }

        return enumDataType.getValue();
    }

    /**
     * 格式化学科的数据类型
     *
     * @param dataType 数据类型
     * @return 格式化学科的数据类型
     * @author majuehao
     * @date 2022/1/11 15:12
     **/
    public static Integer formatSubjectDataType(Integer dataType) {
        if (EnumDataType.文摘.getValue() == dataType) {
            return EnumDataType.外文期刊.getValue();
        }

        return dataType;
    }


    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public Integer getProperty() {
        return property;
    }

    public Long getLanguage() {
        return language;
    }

    /**
     * 根据值获取对应的枚举
     *
     * @param type 传入的值
     * @return 枚举
     * @author majuehao
     * @date 2022/1/11 15:13
     **/
    public static EnumDataType getDataType(int type) {
        for (EnumDataType enumDataType : EnumDataType.values()) {
            if (enumDataType.value == type) {
                return enumDataType;
            }
        }
        return EnumDataType.外文期刊;
    }

    /**
     * 根据值获取对应的枚举
     *
     * @param name 传入的值
     * @return 枚举
     * @author majuehao
     * @date 2022/1/11 15:13
     **/
    public static int getValueByName(String name) {
        for (EnumDataType enumDataType : EnumDataType.values()) {
            if (enumDataType.name.equals(name)) {
                return enumDataType.getValue();
            }
        }
        return EnumDataType.外文期刊.getValue();
    }

    /**
     * 根据值获取对应的枚举
     *
     * @param property 属性
     * @param language 语种
     * @return 枚举
     * @author majuehao
     * @date 2022/1/11 15:13
     **/
    public static EnumDataType getValueByPropertyAndLanguage(Integer property, Long language) {
        if (property.equals(6) || property.equals(7)) {
            for (EnumDataType enumDataType : EnumDataType.values()) {
                if (enumDataType.property.equals(property) && enumDataType.language.equals(language)) {
                    return enumDataType;
                }
            }
        } else {
            if (property.equals(EnumDataBaseProperty.文摘引文.getValue())) {
                return EnumDataType.文摘;
            } else {
                return EnumDataType.其他;
            }
        }
        return EnumDataType.其他;
    }

    /**
     * 获取对应的数据类型
     *
     * @return 核心对应的数据类型
     * @author huxubin
     * @date 2020/9/10 10:49
     */
    public static List<EnumDataType> getCoreDataTypeList() {
        return Lists.newArrayList(外文期刊, 外文图书, 中文期刊, 中文图书);
    }

    /**
     * 获取数据库的类型
     *
     * @return 数据库的类型
     * @author majuehao
     * @date 2022/1/11 15:14
     **/
    public static List<Integer> getDataBaseType() {
        return Lists.newArrayList(外文期刊.value, 外文图书.value, 中文期刊.value, 中文图书.value, 其他.value);
    }

}
