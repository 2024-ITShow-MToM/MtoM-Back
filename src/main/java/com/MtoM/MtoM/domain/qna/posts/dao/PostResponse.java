package com.MtoM.MtoM.domain.qna.posts.dao;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostResponse {
    private Long postId;
    private String img;
    private List<PostUserResponse> user;
    private String title;
    private String content;
    private String createdAt;
    private int view;
    private String hashtags;
    private int heartCount;
    private int commentCount;
    private List<CommentResponse> comments;
}
