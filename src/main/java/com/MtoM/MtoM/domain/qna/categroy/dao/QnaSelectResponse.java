package com.MtoM.MtoM.domain.qna.categroy.dao;

import lombok.Data;

import java.util.List;

@Data
public class QnaSelectResponse {
    private Long selectId;
    private String select = "🧐양자택일"; // `🧐양자택일` 문자 고정
    private String title;
    private String createdAt;
    private int participants;
    private String userSelect; // 유저가 선택한 옵션 변환
    private List<VoteOptionResponse> options;
}
