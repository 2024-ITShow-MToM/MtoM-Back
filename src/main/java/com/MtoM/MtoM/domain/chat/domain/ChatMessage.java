package com.MtoM.MtoM.domain.chat.domain;

import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@Entity
@Data
public class ChatMessage {


    @PrePersist
    public void prePersist() {
        this.timestamp = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    public String getFormattedTimestamp() {
        return timestamp.atZone(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonIgnore
    private UserDomain sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    @JsonIgnore
    private UserDomain receiver;

    private String message;
    private LocalDateTime timestamp;
    private boolean isRead = false;  // 메시지 읽었는지 안읽었는지 확인하는 필드 

    // 직렬화/역직렬화를 위한 메소드 추가
    public String getSenderId() {
        return sender != null ? sender.getId() : null;
    }

    public String getReceiverId() {
        return receiver != null ? receiver.getId() : null;
    }
}