package com.kcidea.erms.enums.database;

import com.google.common.collect.Lists;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.model.common.IdNameModel;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/7 14:19
 **/
public enum EnumCheckState {

    /**
     * 未提交
     */
    未提交(0, "未提交"),

    /**
     * 待审核
     */
    待审核(1, "待审核"),

    /**
     * 审核通过
     */
    审核通过(2, "通过"),

    /**
     * 审核未通过
     */
    审核未通过(3, "未通过");

    final int value;
    final String name;

    EnumCheckState(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public long getLongValue() {
        return (long)value;
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
    public static EnumCheckState getCheckState(Integer type) {
        for (EnumCheckState enumCheckState : EnumCheckState.values()) {
            if (enumCheckState.value == type) {
                return enumCheckState;
            }
        }
        return EnumCheckState.未提交;
    }

    /**
     * 获取审核结果下拉集合
     *
     * @return 反馈审核结果下拉集合
     * @author majuehao
     * @date 2021/12/7 14:22
     **/
    public static List<IdNameModel> findCheckStateList() {
        List<IdNameModel> list = Lists.newArrayList(new IdNameModel().create(Constant.ALL_LONG_VALUE, "全部"));
        for (EnumCheckState buyPlanState : EnumCheckState.values()) {
            list.add(new IdNameModel().create((long) buyPlanState.getValue(), buyPlanState.getName()));
        }
        return list;
    }
}
