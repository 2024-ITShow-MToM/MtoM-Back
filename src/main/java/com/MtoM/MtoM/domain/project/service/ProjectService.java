package com.MtoM.MtoM.domain.project.service;

import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.project.domain.ProjectRedisDomain;
import com.MtoM.MtoM.domain.project.dto.RegisterProjectRequestDto;
import com.MtoM.MtoM.domain.project.repository.ProjectRedisRepository;
import com.MtoM.MtoM.domain.project.repository.ProjectRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectRedisRepository projectRedisRepository;

    public void registerProject(RegisterProjectRequestDto requestDto){
        ProjectDomain projectDomain = projectRepository.save(requestDto.toEntity());

        Long projectId = projectDomain.getId();
        ProjectRedisDomain projectRedisDomain = requestDto.toRedis(projectId);
        projectRedisRepository.save(projectRedisDomain);

    }
}

