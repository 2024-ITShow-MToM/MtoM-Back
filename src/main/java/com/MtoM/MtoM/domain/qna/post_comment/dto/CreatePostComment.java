package com.MtoM.MtoM.domain.qna.post_comment.dto;

import lombok.Data;

@Data
public class CreatePostComment {
    private Long postId;
    private String userId;
    private String content;
}
