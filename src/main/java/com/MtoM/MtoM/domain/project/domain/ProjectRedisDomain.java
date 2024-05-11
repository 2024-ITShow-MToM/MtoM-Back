package com.MtoM.MtoM.domain.project.domain;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "projects")
public class ProjectRedisDomain {
    @Id
    private Integer id;
    private Integer front_member;
    private Integer current_front_member;
    private Integer back_member;
    private Integer currnet_back_member;
    private Integer disign_member;
    private Integer current_disign_member;
    private Integer promoter_member;
    private Integer current_promoter_member;
}
