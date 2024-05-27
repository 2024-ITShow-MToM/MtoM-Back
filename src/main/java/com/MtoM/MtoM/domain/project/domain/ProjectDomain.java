package com.MtoM.MtoM.domain.project.domain;

import com.MtoM.MtoM.domain.user.domain.SkillDomain;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "projects")
public class ProjectDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserDomain user;

    private String img;

    private String title;

    private String description;

    private String recruitment_start;

    private String recruitment_end;

    private String work_start;

    private String work_end;

    private Long frontend_personnel;

    private Long backend_personnel;

    private Long designer_personnel;

    private Long promoter_personnel;

    @Column(columnDefinition = "TEXT")
    private String  introduction;

    private Boolean is_matching;

    @JsonIgnore
    @OneToMany(mappedBy = "project",  cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<MatchingProjectDomain> matchingProjectDomainList;
}
