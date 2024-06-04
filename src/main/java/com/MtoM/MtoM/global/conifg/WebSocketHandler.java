package com.MtoM.MtoM.global.conifg;

import com.MtoM.MtoM.domain.chat.service.RedisMessagePublisher;
import com.MtoM.MtoM.domain.chat.domain.ChatMessage;
import com.MtoM.MtoM.domain.chat.service.ChatMessageService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new ArrayList<>();
    private final RedisMessagePublisher redisMessagePublisher;
    private final ChatMessageService chatMessageService;

    public WebSocketHandler(RedisMessagePublisher redisMessagePublisher, ChatMessageService chatMessageService) {
        this.redisMessagePublisher = redisMessagePublisher;
        this.chatMessageService = chatMessageService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            // Assume the message payload is in the format "senderId:receiverId:message"
            String payload = message.getPayload();
            String[] parts = payload.split(":", 3);
            if (parts.length == 3) {
                String senderId = parts[0].trim();
                String receiverId = parts[1].trim();
                String chatMessage = parts[2].trim();

                // Save message to database
                ChatMessage savedMessage = chatMessageService.saveMessage(senderId, receiverId, chatMessage);

                // Create a new message with savedMessage details to send to Redis
                String redisMessage = senderId + ":" + receiverId + ":" + savedMessage.getMessage();

                // Publish to Redis
                redisMessagePublisher.publish(redisMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    public void sendMessageToAll(String message) throws Exception {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }
}