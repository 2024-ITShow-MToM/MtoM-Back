package com.MtoM.MtoM.domain.user.dto;

import com.MtoM.MtoM.domain.user.domain.Gender;
import com.MtoM.MtoM.domain.user.domain.Major;
import com.MtoM.MtoM.domain.user.domain.SkillDomain;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import lombok.Getter;
import org.apache.catalina.User;

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
    private String skill_name;
    private Integer skill_score;
    private String personal;
    private String imogi;

    public SkillDomain toSkillEntity(){
        return SkillDomain.builder()
                .user(userId)
                .skill_name(skill_name)
                .skill_score(skill_score)
                .build();
    }
}

