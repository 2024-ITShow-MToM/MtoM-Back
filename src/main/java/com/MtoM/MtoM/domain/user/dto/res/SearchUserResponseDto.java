package com.MtoM.MtoM.domain.user.dto.res;

import com.MtoM.MtoM.global.util.enums.Gender;
import com.MtoM.MtoM.global.util.enums.Major;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.dto.Skill;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SearchUserResponseDto {
    private String userId;
    private String name;
    private Long studentId;
    private String birthday;
    private Gender gender;
    private String phonenumber;
    private Major major;
    private String email;
    private String profile;
    private String mbti;
    private String personal;
    private String imogi;
    private String information;
    private String mentoring_topics;
    private List<Skill> skills;

    public SearchUserResponseDto(UserDomain user){
        this.userId = user.getId();
        this.name = user.getName();
        this.studentId = user.getStudent_id();
        this.birthday = user.getBirthday();
        this.gender = user.getGender();
        this.phonenumber = user.getPhonenumber();
        this.major = user.getMajor();
        this.email = user.getEmail();
        this.profile = user.getProfile();
        this.mbti = user.getMbti();
        this.personal = user.getPersonal();
        this.imogi = user.getImogi();
        this.information = user.getIntroduction();
        this.mentoring_topics = user.getMentoring_topics();
        this.skills = user.getSkillDomainList().stream()
                .map(skillDomain -> new Skill(skillDomain.getSkill_name(), skillDomain.getSkill_score()))
                .collect(Collectors.toList());
    }
}
