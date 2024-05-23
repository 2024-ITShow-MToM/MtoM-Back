package com.MtoM.MtoM.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Skill {
    private String skill_name;
    private Integer skill_score;
}
