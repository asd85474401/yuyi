package com.kcidea.erms.enums.user;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
public enum EnumLoginAction {

    /**
     * 执行登录验证
     */
    Normal("0", "执行登录验证"),

    /**
     * 跳过登录验证
     */
    Skip("1", "跳过登录验证");

    final String key;
    final String desc;

    EnumLoginAction(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return this.key;
    }

    public String getDesc() {
        return this.desc;
    }
}
