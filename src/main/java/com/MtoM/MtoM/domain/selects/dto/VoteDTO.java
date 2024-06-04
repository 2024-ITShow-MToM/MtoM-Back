package com.MtoM.MtoM.domain.selects.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteDTO {
    private Long selectId; // SelectDomain의 id
    private String selectedOption; // 선택한 옵션 (content1 또는 content2)
}
