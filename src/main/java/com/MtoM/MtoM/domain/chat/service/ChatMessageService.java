package com.MtoM.MtoM.domain.chat.service;


import com.MtoM.MtoM.domain.chat.domain.ChatMessage;
import com.MtoM.MtoM.domain.chat.repository.ChatMessageRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        return chatMessageRepository.findAll();
    }
}