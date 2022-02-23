package com.kcidea.erms.config;

import com.kcidea.erms.websocket.WebSocketClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author huxubin
 * @version 1.0
 * @date 2020/12/28
 **/
@Configuration
@EnableWebSocket
public class WebSocketConfig {

    @Bean
    public WebSocketClient reverseWebSocketEndpoint() {
        return new WebSocketClient();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
