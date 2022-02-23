package com.kcidea.erms.websocket;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.model.websocket.WebSocketDataModel;
import com.kcidea.erms.service.common.FactoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huxubin
 * @version 1.0
 * @date 2020/10/30
 **/
@ServerEndpoint(value = "/websocket/{uuid}")
@Component
@Slf4j
public class WebSocketClient {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static final ConcurrentHashMap<String, WebSocketClient> WEB_SOCKET_MAP = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收userId
     */
    private String uuid = "";

    private static FactoryService factoryService;

    @Autowired
    public void setFactoryService(FactoryService factoryService) {
        WebSocketClient.factoryService = factoryService;
    }

    /**
     * 连接建立成功调用的方法
     * @param session session
     * @param uuid uuid
     * @author huxubin
     * @date 2021/8/19 17:57
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uuid") String uuid) {
        this.session = session;
        this.uuid = uuid;
        if (WEB_SOCKET_MAP.containsKey(uuid)) {
            //移除这个uuid
            WEB_SOCKET_MAP.remove(uuid);
            //更新新的uuid信息
            WEB_SOCKET_MAP.put(uuid, this);
        } else {
            //加入uuid
            WEB_SOCKET_MAP.put(uuid, this);
            //增加在线人数
            addOnlineCount();
        }

        log.info("用户连接:" + uuid + ",当前在线人数为:" + getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (Exception e) {
            log.error("用户:" + uuid + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     * @author huxubin
     * @date 2021/8/19 17:57
     */
    @OnClose
    public void onClose() {
        if (WEB_SOCKET_MAP.containsKey(uuid)) {
            //移除这个uuid
            WEB_SOCKET_MAP.remove(uuid);
            //减少在线人数
            subOnlineCount();
        }
        log.info("用户退出:" + uuid + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @author huxubin
     * @date 2021/8/19 17:57
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("接收到用户:" + uuid + ",报文:" + message);
        //可以群发消息
        //消息保存到数据库、redis
        if (!Strings.isNullOrEmpty(message)) {
            try {
                //解析发送的报文
                WebSocketDataModel webSocketDataModel = JSON.parseObject(message, WebSocketDataModel.class);

                //如果解析不出来JSON数据，或者没有解析出请求的api，那么就是有错误
                if(webSocketDataModel == null || Strings.isNullOrEmpty(webSocketDataModel.getApi())){
                    WebSocketClient.sendInfo(Vm.ERROR_PARAMS,uuid);
                    return;
                }

                factoryService.executeTask(uuid, webSocketDataModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误后调用的方法
     * @param error   错误的消息
     * @author huxubin
     * @date 2021/8/19 17:57
     */
    @OnError
    public void onError(Throwable error) {
        log.error("用户错误:" + this.uuid + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     * @param message 推送的消息
     * @author huxubin
     * @date 2021/8/19 17:58
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (Exception ex) {
            log.error("WebSocket发送消息失败，原因：" + ex.getMessage(), ex);
        }
    }

    /**
     * 发送自定义消息
     * @param message 发送的消息
     * @param uuid 对应的用户uuid
     * @author huxubin
     * @date 2021/8/19 17:59
     */
    public static void sendInfo(String message, @PathParam("uuid") String uuid) {
        log.info("发送消息到:" + uuid + "，报文:" + message);
        if (!Strings.isNullOrEmpty(uuid) && WEB_SOCKET_MAP.containsKey(uuid)) {
            WEB_SOCKET_MAP.get(uuid).sendMessage(message);
        } else {
            log.error("用户" + uuid + ",不在线！");
        }
    }

    /**
     * 获取在线人数
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 增加在线人数
     */
    public static synchronized void addOnlineCount() {
        WebSocketClient.onlineCount++;
    }

    /**
     * 减少在线人数
     */
    public static synchronized void subOnlineCount() {
        WebSocketClient.onlineCount--;
    }

}
