package com.MtoM.MtoM.domain.user.dto;

import com.MtoM.MtoM.domain.user.domain.Gender;
import com.MtoM.MtoM.domain.user.domain.Major;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import lombok.Getter;

@Getter
public class RegisterProfileInfoDto {
    private String email;
    private String name;
    private Long student_id;
    private String birthday;
    private Gender gender;
    private String phonenumber;
    private Major major;
    private String mbti;
    private String skill_name;
    private String skill_score;
    private String personal;
    private String imogi;

    public UserDomain toEntity(){
        return UserDomain.builder()
                .name(name)
                .studnet_id(student_id)
                .birthday(birthday)
                .gender(gender)
                .phonenumber(phonenumber)
                .major(major)
                .mbti(mbti)
                .personal(personal)
                .imogi(imogi)
                .build();
    }
}
