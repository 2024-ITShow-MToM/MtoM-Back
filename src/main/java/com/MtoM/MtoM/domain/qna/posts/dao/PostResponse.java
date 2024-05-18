package com.MtoM.MtoM.domain.qna.posts.dao;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostResponse {
    private Long postId;
    private String title;
    private String content;
    private String img;
    private String hashtags;
    private String major;
    private String userName;
    private String userProfile;
    private int commentCount;
    private int heartCount;
    private List<CommentResponse> comments;
}
