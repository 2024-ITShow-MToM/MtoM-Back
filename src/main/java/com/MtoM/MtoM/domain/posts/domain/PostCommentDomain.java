package com.MtoM.MtoM.domain.posts.domain;

import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_comment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostCommentDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PostDomain post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserDomain user;

    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "integer default 0")
    private int hearts;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}