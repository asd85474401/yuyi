package com.kcidea.erms.model.websocket;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/25
 **/
@Data
@Accessors(chain = true)
public class WebSocketDataModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应的api
     */
    private String api;

    /**
     * 对应的uuid
     */
    private String uuid;

    /**
     * 数据
     */
    private Object data;

    public WebSocketDataModel(String api, String uuid, Object data) {
        this.api = api;
        this.uuid = uuid;
        this.data = data;
    }
}
