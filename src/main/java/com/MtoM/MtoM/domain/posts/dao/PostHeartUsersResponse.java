package com.MtoM.MtoM.domain.posts.dao;

import lombok.Data;

import java.util.List;

@Data
public class PostHeartUsersResponse {
    private int heartCount; // 총 좋아요 갯수
    private int commentCount;
    private List<PostUserResponse> users;
}
