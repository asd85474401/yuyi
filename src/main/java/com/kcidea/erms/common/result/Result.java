package com.kcidea.erms.common.result;

import com.kcidea.erms.common.constant.Vm;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
public class Result<T> {

    /**
     * 成功标识
     */
    private Boolean success;

    /**
     * 状态码
     */
    private int status;

    /**
     * 返回消息
     */
    private String msg;

    /**
     * 跳转路径
     */
    private String url;

    /**
     * 返回结果
     */
    private T data;

    public String getUrl() {
        return url;
    }

    public Result<T> setUrl(String url) {
        this.url = url;
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Result<T> setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 返回错误的消息
     *
     * @param message 消息
     * @author huxubin
     * @date 2021/11/9 16:29
     */
    public Result<T> error(String message) {
        this.msg = message;
        this.success = false;
        return this;
    }

    /**
     * 返回错误的消息
     *
     * @param message 消息
     * @author huxubin
     * @date 2021/11/9 16:29
     */
    public Result<T> error(int status, String message) {
        this.msg = message;
        this.success = false;
        this.status = status;
        return this;
    }

    /**
     * 返回查询到的对象
     *
     * @param data 对象信息
     * @author huxubin
     * @date 2021/11/9 16:28
     */
    public Result<T> success(T data) {
        this.msg = Vm.SUCCESS_MSG;
        this.success = true;
        this.setData(data);
        this.status = 200;
        return this;
    }

    /**
     * 返回成功消息，没有对象
     *
     * @author huxubin
     * @date 2021/11/9 16:27
     */
    public void successMsg() {
        this.successMsg(Vm.SUCCESS_MSG);
    }

    /**
     * 返回成功消息，没有对象
     *
     * @param msg 返回消息
     * @author huxubin
     * @date 2021/11/9 16:27
     */
    public Result<T> successMsg(String msg) {
        this.msg = msg;
        this.success = true;
        this.status = 200;
        return this;
    }
}
