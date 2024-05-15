package com.MtoM.MtoM.domain.qna.post.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//  TODO : 유저 기능 구현되면 관계 연결하기
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String img;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String hashtags;
}
