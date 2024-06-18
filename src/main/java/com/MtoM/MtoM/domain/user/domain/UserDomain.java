package com.MtoM.MtoM.domain.user.domain;

import com.MtoM.MtoM.domain.mentor.domain.MentorDomain;
import com.MtoM.MtoM.domain.project.domain.MatchingProjectDomain;
import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.global.util.enums.Gender;
import com.MtoM.MtoM.global.util.enums.Major;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity (name = "users")
public class UserDomain {
    @Id
    private String id;

    private String name;

    private Long student_id;

    private String birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(unique = true)
    private String phonenumber;

    @Enumerated(EnumType.STRING)
    private Major major;

    @JsonIgnore
    private String password;

    @Column(unique = true)
    private String email;

    private String profile;

    private String mbti;

    private String personal;

    private String imogi;

    private String mentoring_topics;

    private String introduction;

    @JsonIgnore
    private String social;

    public UserDomain(String id){
        this.id = id;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user",  cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<ProjectDomain> projectDomainList;

    @JsonIgnore
    @OneToMany(mappedBy = "user",  cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<SkillDomain> skillDomainList;

    @JsonIgnore
    @OneToMany(mappedBy = "user",  cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<MatchingProjectDomain> matchingProjectDomainList;

    @JsonIgnore
    @OneToMany(mappedBy = "mentor",  cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<MentorDomain> mentorDomainList;

    @JsonIgnore
    @OneToMany(mappedBy = "mentee",  cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<MentorDomain> menteeDomainList;


    public void registerProfile(String name, Long student_id, String birthday, Gender gender, String phonenumber, Major major, String mbti, String personal, String imogi, String mentoring_topics, String introduction){
        this.name = name;
        this.student_id = student_id;
        this.birthday = birthday;
        this.gender = gender;
        this.phonenumber = phonenumber;
        this.major = major;
        this.mbti = mbti;
        this.personal = personal;
        this.imogi = imogi;
        this.mentoring_topics = mentoring_topics;
        this.introduction = introduction;
    }

    public void registerProfileImage(String imageUrl){
        this.profile = imageUrl;
    }
}
