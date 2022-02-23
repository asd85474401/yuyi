package com.kcidea.erms.enums.task;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/8/23
 **/
public enum EnumTaskResult {

    /**
     * 执行成功
     */
    SUCCESS("成功"),

    /**
     * 执行失败
     */
    ERROR("失败"),

    /**
     * 执行成功，但是需要警告提示
     */
    WARNING("警告");

    final String value;

    EnumTaskResult(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
