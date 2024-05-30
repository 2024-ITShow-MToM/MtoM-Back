package com.MtoM.MtoM.domain.project.service;

import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.project.dto.res.ListProjectResponseDto;
import com.MtoM.MtoM.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ListProjectService {
    private final ProjectRepository projectRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional(readOnly = true)
    public List<ListProjectResponseDto> execute(){
        List<ProjectDomain> projects = projectRepository.findAll();
        return projects.stream()
                .map(project ->{
                    String redisKey = "project:" + project.getId();
                    Map<Object, Object> redisHash = redisTemplate.opsForHash().entries(redisKey);
                    return new ListProjectResponseDto(project, redisHash);
                })
                .collect(Collectors.toList());
    }
}
