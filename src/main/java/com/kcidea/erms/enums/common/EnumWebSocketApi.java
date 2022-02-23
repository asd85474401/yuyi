package com.kcidea.erms.enums.common;

import com.google.common.base.Strings;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/25
 **/
public enum EnumWebSocketApi {

    /**
     * 数据库评估列表同步
     */
    数据库评估列表同步(0, "DataBaseSynchrony"),

    /**
     * 数据库信息列表导入
     */
    数据库信息列表导入(1, "DataBaseImport"),

    /**
     * 数据库采购列表导入
     */
    数据库采购列表导入(2, "DataBaseBuyImport"),

    /**
     * 用户反馈处理列表导入
     */
    用户反馈处理列表导入(3, "UserFeedbackListImport"),

    ;

    final int value;
    final String name;

    EnumWebSocketApi(int value, String name) {
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
     * 根据api获取对应的枚举
     *
     * @param api 请求的api
     * @return 对应的枚举
     * @author huxubin
     * @date 2021/11/25 16:23
     */
    public static EnumWebSocketApi getWebSocketApi(String api) {

        if (Strings.isNullOrEmpty(api)) {
            return null;
        }

        for (EnumWebSocketApi enumWebSocketApi : EnumWebSocketApi.values()) {
            if (enumWebSocketApi.name.equals(api)) {
                return enumWebSocketApi;
            }
        }
        return null;
    }
}
