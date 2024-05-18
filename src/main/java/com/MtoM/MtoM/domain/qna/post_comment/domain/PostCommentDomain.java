package com.MtoM.MtoM.domain.qna.post_comment.domain;

import com.MtoM.MtoM.domain.qna.posts.domain.PostDomain;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_comment")
public class PostCommentDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PostDomain post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserDomain user;

    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}