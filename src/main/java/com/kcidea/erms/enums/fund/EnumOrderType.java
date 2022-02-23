package com.kcidea.erms.enums.fund;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.model.common.IdNameModel;

import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
public enum EnumOrderType {

    /**
     * 停订数据库
     */
    停订数据库(0, "停订"),

    /**
     * 新订数据库
     */
    新订数据库(1, "新订"),

    /**
     * 续订数据库
     */
    续订数据库(2, "续订"),

    /**
     * 买断数据库
     */
    买断数据库(3, "买断"),

    /**
     * 免费数据库
     */
    免费数据库(5, "免费");

    ;

    final int value;
    final String name;

    EnumOrderType(int value, String name) {
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
     * 获取订购类型下拉集合
     *
     * @return 订购类型下拉集合
     * @author yeweiwei
     * @date 2021/11/16 14:11
     */
    public static List<IdNameModel> findOrderTypeSelectList() {
        List<IdNameModel> list = Lists.newArrayList(new IdNameModel().create(Constant.ALL_LONG_VALUE, "全部"));
        for (EnumOrderType orderType : EnumOrderType.values()) {
            list.add(new IdNameModel().create((long) orderType.getValue(), orderType.getName()));
        }
        return list;
    }

    /**
     * 通过名称获得value
     *
     * @param name 名称
     * @return value
     * @author yeweiwei
     * @date 2021/11/16 16:26
     */
    public static Integer getOrderType(String name) {
        if (Strings.isNullOrEmpty(name)) {
            return null;
        }
        name = name.trim();
        for (EnumOrderType orderType : EnumOrderType.values()) {
            if (name.equals(orderType.getName())) {
                return orderType.getValue();
            }
        }
        return null;
    }

    /**
     * 根据类型的值获取对应的枚举
     *
     * @param value 值
     * @return 对应的枚举
     * @author majuehao
     * @date 2021/12/8 15:01
     **/
    public static EnumOrderType getEnumOrderType(int value) {
        for (EnumOrderType enumOrderType : EnumOrderType.values()) {
            if (value == enumOrderType.value) {
                return enumOrderType;
            }
        }
        return null;
    }
}
