package com.MtoM.MtoM.domain.user.dto.req;

import com.MtoM.MtoM.global.util.enums.Gender;
import com.MtoM.MtoM.global.util.enums.Major;
import com.MtoM.MtoM.domain.user.domain.SkillDomain;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.dto.Skill;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class RegisterProfileInfoDto {
    private String userId;
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
    private String introduction;

    public List<SkillDomain> toSkillEntity(UserDomain user){
        return skills.stream()
                .map(skill -> SkillDomain.builder()
                        .user(user)
                        .skill_name(skill.getSkill_name())
                        .skill_score(skill.getSkill_score())
                        .build())
                .collect(Collectors.toList());
    }
}
