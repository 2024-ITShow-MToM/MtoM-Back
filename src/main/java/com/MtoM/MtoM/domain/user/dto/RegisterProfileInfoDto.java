package com.MtoM.MtoM.domain.user.dto;

import com.MtoM.MtoM.domain.user.domain.Gender;
import com.MtoM.MtoM.domain.user.domain.Major;
import com.MtoM.MtoM.domain.user.domain.SkillDomain;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import lombok.Getter;
import org.apache.catalina.User;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RegisterProfileInfoDto {
    private UserDomain userId;
    private String name;
    private Long student_id;
    private String birthday;
    private Gender gender;
    private String phonenumber;
    private Major major;
    private String mbti;
    private List<Skill> skills;
    private String personal;
    private String imogi;
    private String mentoring_topics;

    public List<SkillDomain> toSkillEntity(){
        return skills.stream()
                .map(skill -> SkillDomain.builder()
                        .user(userId)
                        .skill_name(skill.getSkill_name())
                        .skill_score(skill.getSkill_score())
                        .build())
                .collect(Collectors.toList());
    }
}
