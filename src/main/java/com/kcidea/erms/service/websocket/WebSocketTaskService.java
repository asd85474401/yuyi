package com.kcidea.erms.service.websocket;

import com.kcidea.erms.enums.common.EnumWebSocketApi;
import com.kcidea.erms.model.user.UserModel;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/25
 **/
public interface WebSocketTaskService {

    /**
     * 获取WebSocketApi工厂
     *
     * @return WebSocketApi工厂
     * @author huxubin
     * @date 2021/11/25 16:11
     */
    EnumWebSocketApi getWebSocketApi();

    /**
     * 执行WebSocket任务
     *
     * @param uuid      操作人uuid
     * @param userModel 请求用户
     * @param jsonData  请求的数据
     * @author huxubin
     * @date 2021/11/25 15:52
     */
    void executeTask(String uuid, UserModel userModel, Object jsonData);

    /**
     * 校验权限
     *
     * @param uuid      操作人uuid
     * @param userModel 请求用户
     * @author huxubin
     * @date 2021/11/25 15:52
     */
    void checkAdminMenu(String uuid, UserModel userModel);
}
