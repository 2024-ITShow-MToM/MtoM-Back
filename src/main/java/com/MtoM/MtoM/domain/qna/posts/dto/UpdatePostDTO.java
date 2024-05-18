package com.MtoM.MtoM.domain.qna.posts.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdatePostDTO {
    private String title;
    private String content;
    private String hashtags;
    private MultipartFile img;
}