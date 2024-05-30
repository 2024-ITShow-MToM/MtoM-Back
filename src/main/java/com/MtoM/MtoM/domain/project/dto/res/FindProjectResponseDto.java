package com.MtoM.MtoM.domain.project.dto.res;

import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import lombok.Getter;

import java.util.Map;

@Getter
public class FindProjectResponseDto {
    private Long id;
    private String userId;
    private String title;
    private String description;
    private String recruitment_start;
    private String recruitment_end;
    private String work_start;
    private String work_end;
    private Long frontend_personnel;
    private Long current_frontend;
    private Long backend_personnel;
    private Long current_backend;
    private Long designer_personnel;
    private Long current_designer;
    private Long promoter_personnel;
    private Long current_promoter;
    private String introduction;
    private Boolean is_matching;
    private String img;

    public FindProjectResponseDto(ProjectDomain projectDomain, Map<Object, Object> redisValue) {
        this.id = projectDomain.getId();
        this.userId = projectDomain.getUser().getId();
        this.title = projectDomain.getTitle();
        this.description = projectDomain.getDescription();
        this.recruitment_start = projectDomain.getRecruitment_start();
        this.recruitment_end = projectDomain.getRecruitment_end();
        this.work_start = projectDomain.getWork_start();
        this.work_end = projectDomain.getWork_end();
        this.frontend_personnel = projectDomain.getFrontend_personnel();
        this.current_frontend = getLongFromRedisValue(redisValue, "currentFrontendPersonnel");
        this.backend_personnel = projectDomain.getBackend_personnel();
        this.current_backend = getLongFromRedisValue(redisValue, "currentBackendPersonnel");
        this.designer_personnel = projectDomain.getDesigner_personnel();
        this.current_designer = getLongFromRedisValue(redisValue, "currentDesignerPersonnel");
        this.promoter_personnel = projectDomain.getPromoter_personnel();
        this.current_promoter = getLongFromRedisValue(redisValue, "currentPromoterPersonnel");
        this.introduction = projectDomain.getIntroduction();
        this.is_matching = projectDomain.getIs_matching();
        this.img = projectDomain.getImg();
    }

    private Long getLongFromRedisValue(Map<Object, Object> redisValue, String key) {
        Object value = redisValue.get(key);
        return (Long) value;
    }
}
