package com.kcidea.erms.common.result;

import com.google.common.collect.Lists;
import com.kcidea.erms.common.constant.Vm;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态
     */
    private Boolean success;

    /**
     * 返回数据
     */
    private List<T> data;

    /**
     * 返回消息
     */
    private String msg;

    /**
     * 跳转的url
     */
    private String url;

    /**
     * 总数
     */
    private Long count;

    /**
     * 空数据
     *
     * @author huxubin
     * @date 2021/11/9 19:21
     */
    public void empty() {
        this.msg = Vm.NO_DATA;
        this.success = true;
        this.setData(Lists.newArrayList());
    }

    /**
     * 返回错误
     *
     * @param message 错误消息
     * @author huxubin
     * @date 2021/11/9 19:21
     */
    public PageResult<T> error(String message) {
        this.msg = message;
        this.success = false;
        this.setData(Lists.newArrayList());
        return this;
    }

    /**
     * 请求成功的消息
     *
     * @param data  数据
     * @param count 总量
     * @author huxubin
     * @date 2021/11/9 19:22
     */
    public PageResult<T> success(List<T> data, long count) {
        this.msg = Vm.SUCCESS_MSG;
        this.success = true;
        this.setData(data);
        this.count = count;
        return this;
    }
}
