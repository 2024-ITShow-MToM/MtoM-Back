package com.MtoM.MtoM.domain.qna.categroy.dao;

import lombok.Data;

import java.util.List;

@Data
public class QnaSelectResponse {
    private Long selectId;
    private String select = "🧐양자택일"; // `🧐양자택일` 문자 고정
    private String title;
    private String date;
    private Long participants;
    private List<VoteOptionResponse> options;
}
