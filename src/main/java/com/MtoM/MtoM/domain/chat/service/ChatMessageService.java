package com.MtoM.MtoM.domain.chat.service;


import com.MtoM.MtoM.domain.chat.domain.ChatMessage;
import com.MtoM.MtoM.domain.chat.dto.ChatParticipantInfo;
import com.MtoM.MtoM.domain.chat.repository.ChatMessageRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ChatMessage saveMessage(String senderId, String receiverId, String message) {
        Optional<UserDomain> senderOptional = userRepository.findById(senderId);
        Optional<UserDomain> receiverOptional = userRepository.findById(receiverId);

        if (!senderOptional.isPresent() || !receiverOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        UserDomain sender = senderOptional.get();
        UserDomain receiver = receiverOptional.get();

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setReceiver(receiver);
        chatMessage.setMessage(message);
        chatMessage.setTimestamp(LocalDateTime.now());

        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getMessages(String senderId, String receiverId) {
        UserDomain sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        UserDomain receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));
        List<ChatMessage> messages = chatMessageRepository.findBySenderAndReceiver(sender, receiver);
        messages.forEach(message -> {
            message.setRead(true);
            chatMessageRepository.save(message);
        });
        return messages;
    }

    public List<ChatMessage> getMessagesForUser(String userId) {
        UserDomain receiver = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Receiver not found"));
        return chatMessageRepository.findByReceiver(receiver);
    }

    public ChatMessage getLastMessageForUser(String userId) {
        UserDomain receiver = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Receiver not found"));
        return chatMessageRepository.findTopByReceiverOrderByTimestampDesc(receiver);
    }

    public long countUnreadMessages(String userId) {
        UserDomain receiver = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Receiver not found"));
        return chatMessageRepository.findByReceiverAndIsReadFalse(receiver).size();
    }

    public List<ChatParticipantInfo> getChatParticipantsInfo(String userId) {
        UserDomain user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<UserDomain> chatPartners = chatMessageRepository.findChatPartners(user);

        List<ChatParticipantInfo> participantsInfo = new ArrayList<>();
        for (UserDomain partner : chatPartners) {
            ChatMessage lastMessage = chatMessageRepository.findTopByUsersOrderByTimestampDesc(user, partner);
            long unreadMessageCount = chatMessageRepository.findByReceiverAndIsReadFalse(partner).size();

            ChatParticipantInfo info = new ChatParticipantInfo();
            info.setUserId(partner.getId());
            info.setLastMessage(lastMessage != null ? lastMessage.getMessage() : null);
            info.setUnreadMessageCount(unreadMessageCount);
            info.setLastMessageTime(lastMessage != null ? lastMessage.getTimestamp() : null);

            participantsInfo.add(info);
        }

        return participantsInfo;
    }
}