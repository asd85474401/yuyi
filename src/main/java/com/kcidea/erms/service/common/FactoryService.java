package com.kcidea.erms.service.common;

import com.kcidea.erms.model.websocket.WebSocketDataModel;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/12/9
 **/
public interface FactoryService {

    /**
     * 执行WebSocket任务
     *
     * @param uuid               请求用户的uuid
     * @param webSocketDataModel 请求的数据
     * @author huxubin
     * @date 2021/11/25 15:52
     */
    void executeTask(String uuid, WebSocketDataModel webSocketDataModel);
}
