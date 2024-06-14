package com.MtoM.MtoM.domain.chat.controller;

import com.MtoM.MtoM.domain.chat.domain.ChatMessage;
import com.MtoM.MtoM.domain.chat.dto.ChatParticipantInfo;
import com.MtoM.MtoM.domain.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;

    @GetMapping("/messages")
    public List<ChatMessage> getMessages(@RequestParam String senderId, @RequestParam String receiverId) {
        List<ChatMessage> senderToReceiverMessages = chatMessageService.getMessages(senderId, receiverId);
        List<ChatMessage> receiverToSenderMessages = chatMessageService.getMessages(receiverId, senderId);

        // 모든 메시지를 한 리스트에 모은 후 최신순으로 정렬
        List<ChatMessage> allMessages = new ArrayList<>();
        allMessages.addAll(senderToReceiverMessages);
        allMessages.addAll(receiverToSenderMessages);
        allMessages.sort(Comparator.comparing(ChatMessage::getTimestamp));

        return allMessages;
    }

    @GetMapping("/chat")
    public String getChatPage() {
        return "chat";
    }

    @GetMapping("/chat/notifications")
    public List<ChatParticipantInfo> getNotifications(@RequestParam String userId) {
        return chatMessageService.getChatParticipantsInfo(userId);
    }
}