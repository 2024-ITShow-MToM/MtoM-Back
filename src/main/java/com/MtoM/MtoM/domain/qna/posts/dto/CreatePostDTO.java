package com.MtoM.MtoM.domain.qna.posts.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreatePostDTO {
    private String userId;
    private MultipartFile img;
    private String title;
    private String content;
    private String hashtags;
}