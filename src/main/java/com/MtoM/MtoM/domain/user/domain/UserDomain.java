package com.MtoM.MtoM.domain.user.domain;

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
    @Column
    private String id;

    @Column
    private String name;

    @Column
    private Long student_id;

    @Column
    private String birthday;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(unique = true)
    private String phonenumber;

    @Column
    @Enumerated(EnumType.STRING)
    private Major major;

    @JsonIgnore
    @Column
    private String password;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    @Column
    private String profile;

    @Column
    private String mbti;

    @Column
    private String personal;

    @Column
    private String imogi;

    @JsonIgnore
    @Column
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
}
