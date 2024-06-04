package com.MtoM.MtoM.domain.chat.domain;

import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
public class ChatMessage {

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

    // 직렬화/역직렬화를 위한 메소드 추가
    public String getSenderId() {
        return sender != null ? sender.getId() : null;
    }

    public String getReceiverId() {
        return receiver != null ? receiver.getId() : null;
    }
}