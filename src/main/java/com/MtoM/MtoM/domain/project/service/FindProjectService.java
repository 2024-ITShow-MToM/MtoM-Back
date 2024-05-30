package com.MtoM.MtoM.domain.project.service;

import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.project.dto.res.FindProjectResponseDto;
import com.MtoM.MtoM.domain.project.repository.ProjectRepository;
import com.MtoM.MtoM.global.exception.ProjectNotFoundException;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class FindProjectService {
    private final ProjectRepository projectRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional(readOnly = true)
    public FindProjectResponseDto execute(Long projectId){
        ProjectDomain project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("project not found", ErrorCode.PROJECT_NOTFOUND));

        String redisKey = "project:" + project.getId();
        Map<Object, Object> redisHash = redisTemplate.opsForHash().entries(redisKey);

        return new FindProjectResponseDto(project, redisHash);
    }
}
