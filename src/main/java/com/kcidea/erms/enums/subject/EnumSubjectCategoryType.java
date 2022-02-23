package com.kcidea.erms.enums.subject;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kcidea.erms.enums.database.EnumDataType;

import java.util.List;
import java.util.Set;

/**
 * @Title 学科类型的枚举
 * @Des
 * @Author yqliang
 * @Date 2020/4/15 9:38
 * @Version 1.0
 **/
public enum EnumSubjectCategoryType {


    /**
     * 教育部学科
     */
    教育部学科(10, "教育部学科", EnumDataType.全部.getValue()),

    /**
     * 教育部学科门类
     */
    教育部学科门类(11, "教育部学科门类", EnumDataType.全部.getValue()),


    /**
     * 教育部一级学科
     */
    教育部一级学科(12, "教育部一级学科", EnumDataType.全部.getValue()),

    /**
     * 教育部二级学科
     */
    教育部二级学科(13, "教育部二级学科", EnumDataType.全部.getValue()),

    /**
     * JCR
     */
    JCR(1010, "JCR学科", EnumDataType.外文期刊.getValue()),

    /**
     * ESI
     */
    ESI(1020, "ESI学科", EnumDataType.外文期刊.getValue()),


    /**
     * SCOPUS
     */
    SCOPUS(1030, "Scopus", EnumDataType.外文期刊.getValue()),


    /**
     * EI
     */
    EI(1040, "EI", EnumDataType.外文期刊.getValue()),
    /**
     * CCF
     */
    CCF(1050, "CCF", EnumDataType.外文期刊.getValue()),


    /**
     * WOS
     */
    WOS(1060, "WOS", EnumDataType.外文期刊.getValue()),
    /**
     * AHCI
     */
    AHCI(1061, "AHCI", EnumDataType.外文期刊.getValue()),
    /**
     * SSCI
     */
    SSCI(1062, "SSCI", EnumDataType.外文期刊.getValue()),
    /**
     * SCIE
     */
    SCIE(1063, "SCIE", EnumDataType.外文期刊.getValue()),
    /**
     * SCI
     */
    ESCI(1064, "ESCI", EnumDataType.外文期刊.getValue()),


    /**
     * 中科院学科
     */
    中科院学科(1070, "中科院学科", EnumDataType.外文期刊.getValue()),

    /**
     * Choice
     */
    Choice(2010, "Choice学科", EnumDataType.外文图书.getValue()),
    /**
     * BCR
     */
    BCR(2020, "BCR学科", EnumDataType.外文图书.getValue()),

    /**
     * EB_Scopus
     */
    EB_Scopus(2030, "Scopus学科", EnumDataType.外文图书.getValue()),


    /**
     * BKCI
     */
    BKCI(2040, "BKCI学科", EnumDataType.外文图书.getValue()),


    /**
     * 中文核心期刊要目总览
     */
    中文核心期刊要目总览(3010, "中文核心期刊要目总览", EnumDataType.中文期刊.getValue()),


    /**
     * 中文社会科学引文索引
     */
    中文社会科学引文索引(3020, "中文社会科学引文索引", EnumDataType.中文期刊.getValue()),


    /**
     * 中国科学引文数据库
     */
    中国科学引文数据库(3030, "中国科学引文数据库", EnumDataType.中文期刊.getValue()),


    /**
     * 中国科技期刊引证报告
     */
    中国科技期刊引证报告(3040, "中国科技期刊引证报告", EnumDataType.中文期刊.getValue()),


    /**
     * CJ_EI
     */
    CJ_EI(3050, "EI", EnumDataType.中文期刊.getValue()),

    /**
     * 自定义学科
     */
    自定义学科(999, "自定义学科", EnumDataType.全部.getValue()),
    /**
     * 组合学科
     */
    组合学科(9999, "组合学科", EnumDataType.全部.getValue());

    int value;
    String name;
    Integer dataType;

    EnumSubjectCategoryType(int value, String name, Integer dataType) {
        this.value = value;
        this.name = name;
        this.dataType = dataType;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    /**
     * 根据值获取对应的枚举
     *
     * @param value 传入的值
     * @return 枚举
     */
    public static EnumSubjectCategoryType getSubjectType(int value) {
        for (EnumSubjectCategoryType enumSubjectType : EnumSubjectCategoryType.values()) {
            if (enumSubjectType.value == value) {
                return enumSubjectType;
            }
        }
        return EnumSubjectCategoryType.教育部学科;
    }

    public static List<EnumSubjectCategoryType> getCategoryTypeListByDataType(EnumDataType dataType) {
        List<EnumSubjectCategoryType> ret = Lists.newArrayList();
        switch (dataType) {
            case 外文期刊: {
                ret = Lists.newArrayList(教育部学科, JCR, ESI, AHCI, SSCI, SCIE, ESCI);
                break;
            }
            case 中文期刊: {
                ret = Lists.newArrayList(教育部学科, 中文社会科学引文索引, 中文核心期刊要目总览, 中国科技期刊引证报告, 中国科技期刊引证报告);
                break;
            }
            case 外文图书: {
                ret = Lists.newArrayList(教育部学科, BKCI, Choice, BCR, EB_Scopus);
                break;
            }
            case 中文图书: {
                ret = Lists.newArrayList(教育部学科);
                break;
            }
            default: {
                break;
            }
        }
        return ret;
    }

    /**
     * 获取WOS的学科
     *
     * @return WOS的学科
     * @author huxubin
     * @date 2021/4/28 15:30
     */
    public static Set<EnumSubjectCategoryType> getWOSCategoryTypeList() {
        return Sets.newHashSet(WOS, AHCI, SSCI, SCIE, ESCI);
    }

}
