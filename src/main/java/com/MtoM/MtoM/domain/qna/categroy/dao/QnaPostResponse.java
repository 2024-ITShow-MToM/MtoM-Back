package com.MtoM.MtoM.domain.qna.categroy.dao;

import lombok.Data;

@Data
public class QnaPostResponse {
    private Long postId;
    private String img; // 게시판 img
    private String createdAt; // 2022.03.11 형식
    private String title;
    private String hashtags;
    private int viewCount;
    private int heartCount;
    private int commentCount;
}
