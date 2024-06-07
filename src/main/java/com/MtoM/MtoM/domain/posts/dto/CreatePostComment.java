package com.MtoM.MtoM.domain.posts.dto;

import lombok.Data;

@Data
public class CreatePostComment {
    private Long postId;
    private String userId;
    private String content;
}
