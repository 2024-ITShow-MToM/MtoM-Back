package com.MtoM.MtoM.domain.project.dto;

import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.project.domain.ProjectRedisDomain;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
@NoArgsConstructor
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

    public ProjectRedisDomain toRedis(Long projectId){
        ProjectRedisDomain projectRedisDomain = new ProjectRedisDomain();
        projectRedisDomain.setId(projectId);
        projectRedisDomain.setFront_member(this.frontend_personnel);
        projectRedisDomain.setBack_member(this.backend_personnel);
        projectRedisDomain.setDisign_member(this.designer_personnel);
        projectRedisDomain.setPromoter_member(this.designer_personnel);
        projectRedisDomain.setCurrent_front_member(0L);
        projectRedisDomain.setCurrnet_back_member(0L);
        projectRedisDomain.setCurrent_disign_member(0L);
        projectRedisDomain.setCurrent_promoter_member(0L);

        return projectRedisDomain;
    }
}
