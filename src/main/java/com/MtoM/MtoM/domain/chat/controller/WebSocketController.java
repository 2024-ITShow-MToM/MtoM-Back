package com.MtoM.MtoM.domain.chat.controller;

import com.MtoM.MtoM.domain.chat.domain.ChatMessage;
import com.MtoM.MtoM.domain.chat.dto.ChatMessageDTO;
import com.MtoM.MtoM.domain.chat.service.ChatMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final ChatMessageService chatMessageService;

    public WebSocketController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessageDTO sendMessage(ChatMessageDTO chatMessageDTO) {
        ChatMessage savedMessage = chatMessageService.saveMessage(
                chatMessageDTO.getSenderId(),
                chatMessageDTO.getReceiverId(),
                chatMessageDTO.getMessage()
        );

        ChatMessageDTO response = new ChatMessageDTO();
        response.setSenderId(savedMessage.getSender().getId());
        response.setReceiverId(savedMessage.getReceiver().getId());
        response.setMessage(savedMessage.getMessage());
        response.setTimestamp(savedMessage.getTimestamp());

        return response;
    }
}