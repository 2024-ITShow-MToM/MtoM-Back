package com.MtoM.MtoM.domain.chat.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatParticipantInfo {
    private String userId;
    private String lastMessage;
    private long unreadMessageCount;
    private LocalDateTime lastMessageTime;

}