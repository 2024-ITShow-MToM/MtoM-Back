package com.MtoM.MtoM.domain.project.dto.res;

import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ListProjectResponseDto {
    private Long id;
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
    private String img;

    public ListProjectResponseDto(ProjectDomain projectDomain) {
        this.id = projectDomain.getId();
        this.userId = projectDomain.getUser().getId();
        this.title = projectDomain.getTitle();
        this.description = projectDomain.getDescription();
        this.recruitment_start = projectDomain.getRecruitment_start();
        this.recruitment_end = projectDomain.getRecruitment_end();
        this.work_start = projectDomain.getWork_start();
        this.work_end = projectDomain.getWork_end();
        this.frontend_personnel = projectDomain.getFrontend_personnel();
        this.backend_personnel = projectDomain.getBackend_personnel();
        this.designer_personnel = projectDomain.getDesigner_personnel();
        this.promoter_personnel = projectDomain.getPromoter_personnel();
        this.introduction = projectDomain.getIntroduction();
        this.is_matching = projectDomain.getIs_matching();
        this.img = projectDomain.getImg();
    }
}
