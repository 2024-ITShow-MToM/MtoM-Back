package com.MtoM.MtoM.domain.qna.posts.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostDTO {
    private String userId;
    private String img;
    private String title;
    private String content;
    private String hashtags;
}