package com.MtoM.MtoM.domain.project.domain;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter @Setter
@RedisHash(value = "projects")
public class ProjectRedisDomain {
    @Id
    private Long id;
    private Long front_member;
    private Long current_front_member;
    private Long back_member;
    private Long currnet_back_member;
    private Long disign_member;
    private Long current_disign_member;
    private Long promoter_member;
    private Long current_promoter_member;
}

