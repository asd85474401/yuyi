package com.kcidea.erms.enums.task;

import com.google.common.collect.Lists;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.model.common.IdNameModel;

import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/26
 **/
public enum EnumTaskState {

    /**
     * 未执行
     */
    未执行(0, "未执行"),

    /**
     * 正在执行
     */
    正在执行(1, "正在执行"),

    /**
     * 执行完成
     */
    执行完成(2, "执行完成"),

    /**
     * 执行失败
     */
    执行失败(3, "执行失败");

    final Integer value;
    final String name;

    EnumTaskState(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    /**
     * 查询任务状态下拉集合
     *
     * @return 任务状态下拉集合
     * @author majueho
     * @date 2021/11/29 10:57
     **/
    public static List<IdNameModel> getDropDownList() {
        List<IdNameModel> list = Lists.newArrayList(new IdNameModel().create(Constant.ALL_LONG_VALUE, Constant.ALL_STRING_VALUE));
        for (EnumTaskState taskState : EnumTaskState.values()) {
            list.add(new IdNameModel().create((long) taskState.getValue(), taskState.getName()));
        }
        return list;
    }

    /**
     * 根据类型的值获取对应的枚举
     *
     * @param value 值
     * @return 对应的枚举
     * @author majuehao
     * @date 2021/12/8 15:01
     **/
    public static EnumTaskState getEnumTaskState(int value) {
        for (EnumTaskState enumTaskState : EnumTaskState.values()) {
            if (value == enumTaskState.value) {
                return enumTaskState;
            }
        }
        return null;
    }
}
