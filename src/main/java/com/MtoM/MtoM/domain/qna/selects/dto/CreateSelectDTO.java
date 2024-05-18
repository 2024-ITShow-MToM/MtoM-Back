package com.MtoM.MtoM.domain.qna.selects.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSelectDTO {
    private String userId;
    private String title;
    private String option1;
    private String option2;
}