package com.MtoM.MtoM.domain.notify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotifyResponseDTO {
    private Long id;
    private Long contentId;
    private String content;
    private Boolean isRead;
    private String notificationType;
    private String senderId;
    private LocalDateTime createdAt;
}