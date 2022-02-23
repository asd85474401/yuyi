package com.kcidea.erms.model.websocket;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/25
 **/
@Data
public class WebSocketProgressModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总量
     */
    private Integer totalCount;

    /**
     * 执行完成数量
     */
    private Integer successCount;

    /**
     * 占比
     */
    private BigDecimal rate;

    /**
     * 对应的消息
     */
    private String message;

    public WebSocketProgressModel create(Integer totalCount, Integer successCount, BigDecimal rate, String message) {
        this.totalCount = totalCount;
        this.successCount = successCount;
        this.rate = rate;
        this.message = message;
        return this;
    }
}
