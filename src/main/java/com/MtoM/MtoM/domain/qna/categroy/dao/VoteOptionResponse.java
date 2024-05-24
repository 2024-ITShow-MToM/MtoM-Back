package com.MtoM.MtoM.domain.qna.categroy.dao;

import lombok.Data;

@Data
public class VoteOptionResponse {
    private String option1;
    private Double percentage1;
    private String option2;
    private Double percentage2;
}