package com.MtoM.MtoM.domain.chat.service;

import com.MtoM.MtoM.global.conifg.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {

    private final WebSocketHandler webSocketHandler;

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