package com.MtoM.MtoM.domain.project.domain;

import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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

    @Column
    private String img;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String recruitment_start;

    @Column
    private String recruitment_end;

    @Column
    private String work_start;

    @Column
    private String work_end;

    @Column
    private Long frontend_personnel;

    @Column
    private Long backend_personnel;

    @Column
    private Long designer_personnel;

    @Column
    private Long promoter_personnel;

    @Column(columnDefinition = "TEXT")
    private String  introduction;

    @Column
    private Boolean is_matching;

}
