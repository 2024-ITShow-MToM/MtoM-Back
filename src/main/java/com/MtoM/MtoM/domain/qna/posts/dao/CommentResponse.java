package com.MtoM.MtoM.domain.qna.posts.dao;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentResponse {
    private Long commentId;
    private String userId;
    private String profile; // 댓글을 작성한 유저의 프로필 사진
    private String name; // 학번 + 이름
    private String content;
    private String time; // 몇분전
    private int heartCount; // 댓글 하트 수
}