package com.kcidea.erms.enums.fund;

import com.google.common.collect.Lists;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.model.common.IdNameModel;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/16 9:04
 **/
public enum EnumPayState {

    /**
     * 未支付
     */
    未支付(0, "未支付"),
    /**
     * 已支付部分
     */
    已支付部分(1, "已支付部分"),
    /**
     * 支付完成
     */
    支付完成(2, "支付完成");

    final int value;
    final String name;

    EnumPayState(int value, String name) {
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
     * 获取支付状态下拉集合
     *
     * @return 支付状态下拉集合
     * @author yeweiwei
     * @date 2021/11/16 14:16
     */
    public static List<IdNameModel> findPayStateSelectList() {
        List<IdNameModel> list = Lists.newArrayList(new IdNameModel().create(Constant.ALL_LONG_VALUE, "全部"));
        for (EnumPayState payState : EnumPayState.values()) {
            list.add(new IdNameModel().create((long) payState.getValue(), payState.getName()));
        }
        return list;
    }

    /**
     * 根据类型的值获取对应的类型
     *
     * @param type 类型
     * @return 对应的枚举
     * @author majuehao
     * @date 2021/11/29 14:52
     **/
    public static EnumPayState getEnumPayState(int type) {
        for (EnumPayState enumPayState : EnumPayState.values()) {
            if (type == enumPayState.value) {
                return enumPayState;
            }
        }
        return null;
    }
}
