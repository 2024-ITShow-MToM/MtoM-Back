package com.MtoM.MtoM.domain.chat.service;

import com.MtoM.MtoM.global.conifg.WebSocketHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
@Service
public class RedisMessageSubscriber implements MessageListener {

    private final WebSocketHandler webSocketHandler;

    public RedisMessageSubscriber(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String messageStr = new String(message.getBody());
            webSocketHandler.sendMessageToAll(messageStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}