package com.MtoM.MtoM.domain.qna.posts.dao;

import lombok.Data;

@Data
public class PostUserResponse {
    private String userId; // 유저 아이디
    private String profile; // 유저 프로필 사진
    private String major; // 전공
    private String name; // 학번 + 이름
}
