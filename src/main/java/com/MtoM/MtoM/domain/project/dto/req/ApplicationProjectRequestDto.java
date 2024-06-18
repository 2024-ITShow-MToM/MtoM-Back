package com.MtoM.MtoM.domain.project.dto.req;

import com.MtoM.MtoM.domain.project.domain.MatchingProjectDomain;
import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.global.util.enums.Role;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import lombok.Getter;

@Getter
public class ApplicationProjectRequestDto {
    private String userId;
    private  Long projectId;
    private Role role;
    private String application;

    public MatchingProjectDomain toEntity(UserDomain user, ProjectDomain project){
        return MatchingProjectDomain.builder()
                .user(user)
                .project(project)
                .role(role)
                .application(application)
                .build();
    }
}
