package com.MtoM.MtoM.domain.user.domain;

import com.MtoM.MtoM.domain.project.domain.MatchingProjectDomain;
import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter @Setter
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

    @JsonIgnore
    private String profile;

    private String mbti;

    private String personal;

    private String imogi;

    private String mentoring_topics;

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
}
