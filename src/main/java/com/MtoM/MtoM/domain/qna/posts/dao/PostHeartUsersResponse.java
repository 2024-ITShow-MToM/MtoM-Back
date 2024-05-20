package com.MtoM.MtoM.domain.qna.posts.dao;

import lombok.Data;

import java.util.List;

@Data
public class PostHeartUsersResponse {
    private int heartCount; // 총 종하요 갯수
    private List<PostUserResponse> users;
}
