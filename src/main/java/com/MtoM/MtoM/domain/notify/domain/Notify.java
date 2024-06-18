package com.MtoM.MtoM.domain.notify.domain;

import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.global.util.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private Long contentId;

    private String content;

    @Column(nullable = false)
    private Boolean isRead;

    private LocalDateTime createdAt; // 알림이 생성된 시간

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonIgnore
    private UserDomain sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    @JsonIgnore
    private UserDomain receiver;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now(); // 알림이 생성될 때 시간을 설정
    }
}