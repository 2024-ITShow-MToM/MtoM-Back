package com.MtoM.MtoM.domain.project.dto.req;

import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class RegisterProjectRequestDto {
    private String userId;
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
    private String introduction;
    private Boolean is_matching;
    private MultipartFile image;

    public ProjectDomain toEntity(UserDomain user, String imgUrl){
        return ProjectDomain.builder()
                .user(user)
                .title(title)
                .description(description)
                .recruitment_start(recruitment_start)
                .recruitment_end(recruitment_end)
                .work_start(work_start)
                .work_end(work_end)
                .frontend_personnel(frontend_personnel)
                .backend_personnel(backend_personnel)
                .designer_personnel(designer_personnel)
                .promoter_personnel(promoter_personnel)
                .introduction(introduction)
                .is_matching(false)
                .img(imgUrl)
                .build();
    }

}
