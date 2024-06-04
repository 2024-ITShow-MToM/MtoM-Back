package com.MtoM.MtoM.domain.chat;

import com.MtoM.MtoM.domain.chat.domain.ChatMessage;
import com.MtoM.MtoM.domain.chat.service.ChatMessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatMessageService chatMessageService;

    public ChatController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @GetMapping("/messages")
    public List<ChatMessage> getMessages(@RequestParam String senderId, @RequestParam String receiverId) {
        return chatMessageService.getMessages(senderId, receiverId);
    }

    @GetMapping("/chat")
    public String getChatPage() {
        return "chat";
    }
}