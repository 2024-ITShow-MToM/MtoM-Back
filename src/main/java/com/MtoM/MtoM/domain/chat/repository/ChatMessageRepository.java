package com.MtoM.MtoM.domain.chat.repository;

import com.MtoM.MtoM.domain.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}