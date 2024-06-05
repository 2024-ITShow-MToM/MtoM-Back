package com.MtoM.MtoM.domain.chat.repository;

import com.MtoM.MtoM.domain.chat.domain.ChatMessage;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndReceiver(UserDomain sender, UserDomain receiver);
    List<ChatMessage> findByReceiver(UserDomain receiver);
    ChatMessage findTopByReceiverOrderByTimestampDesc(UserDomain receiver);
    List<ChatMessage> findByReceiverAndIsReadFalse(UserDomain receiver);
}