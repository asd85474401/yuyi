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
public enum EnumTaskType {

    /**
     * 数据库信息列表导入
     */
    数据库信息列表导入(10000, "数据库信息列表导入"),

    /**
     * 数据库评估列表同步
     */
    数据库评估列表同步(20000, "数据库评估列表同步"),

    /**
     * 数据库采购列表导入
     */
    数据库采购列表导入(30000, "数据库采购列表导入"),

    /**
     * 用户反馈处理列表导入
     */
    用户反馈处理列表导入(40000, "用户反馈处理列表导入"),
    ;

    final Integer value;
    final String name;

    EnumTaskType(Integer value, String name) {
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
     * 查询任务类型下拉集合
     *
     * @return 任务类型下拉集合
     * @author majueho
     * @date 2021/11/29 10:57
     **/
    public static List<IdNameModel> getDropDownList() {
        List<IdNameModel> list =
                Lists.newArrayList(new IdNameModel().create(Constant.ALL_LONG_VALUE, Constant.ALL_STRING_VALUE));
        for (EnumTaskType taskType : EnumTaskType.values()) {
            list.add(new IdNameModel().create((long) taskType.getValue(), taskType.getName()));
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
    public static EnumTaskType getEnumTaskType(int value) {
        for (EnumTaskType enumTaskType : EnumTaskType.values()) {
            if (value == enumTaskType.value) {
                return enumTaskType;
            }
        }
        return null;
    }
}
