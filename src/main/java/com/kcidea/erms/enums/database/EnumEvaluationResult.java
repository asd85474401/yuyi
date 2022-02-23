package com.kcidea.erms.enums.database;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
public enum EnumEvaluationResult {

    /**
     * 未评估
     */
    未评估(0, "未评估"),

    /**
     * 通过
     */
    通过(1, "通过"),

    /**
     * 未通过
     */
    未通过(2, "未通过"),

    /**
     * 全部
     */
    全部(999, "全部");

    final int value;
    final String name;

    EnumEvaluationResult(int value, String name) {
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
     * 根据值获取枚举
     *
     * @param value 值
     * @return 枚举
     * @author majuehao
     * @date 2021/12/8 11:00
     */
    public static EnumEvaluationResult getEnumEvaluationResult(int value) {
        for (EnumEvaluationResult enumEvaluationResult : EnumEvaluationResult.values()) {
            if (enumEvaluationResult.getValue() == value) {
                return enumEvaluationResult;
            }
        }
        return null;
    }
}
