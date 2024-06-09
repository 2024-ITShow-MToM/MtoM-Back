package com.MtoM.MtoM.domain.chat.repository;

import com.MtoM.MtoM.domain.chat.domain.ChatMessage;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndReceiver(UserDomain sender, UserDomain receiver);
    List<ChatMessage> findByReceiver(UserDomain receiver);
    ChatMessage findTopByReceiverOrderByTimestampDesc(UserDomain receiver);
    List<ChatMessage> findByReceiverAndIsReadFalse(UserDomain receiver);
    @Query("SELECT DISTINCT c.sender FROM ChatMessage c WHERE c.receiver = :user " +
            "UNION " +
            "SELECT DISTINCT c.receiver FROM ChatMessage c WHERE c.sender = :user")
    List<UserDomain> findChatPartners(@Param("user") UserDomain user);
}