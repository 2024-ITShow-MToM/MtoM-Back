package com.MtoM.MtoM.domain.project.service;

import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.project.dto.res.FindMajorProjectResponseDto;
import com.MtoM.MtoM.domain.project.repository.ProjectRepository;
import com.MtoM.MtoM.global.exception.MajorNotFoundException;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import com.MtoM.MtoM.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FindMajorProjectService {
    private final ProjectRepository projectRepository;
    private final RedisService redisService;
    private final RedisTemplate<String, Object> redisTemplate;

    public List<FindMajorProjectResponseDto> execute(String major){
        List<ProjectDomain> projects = new ArrayList<>();

        switch (major) {
            case "backend":
                projects = projectRepository.findByBackend(); break;
            case "frontend" :
                projects = projectRepository.findByFrontend(); break;
            case "designer" :
                projects = projectRepository.findByDesigner(); break;
            case "promoter" :
                projects = projectRepository.findByPromoter(); break;
            default:
                throw new MajorNotFoundException("major not found", ErrorCode.MAJOR_NOTFOUND);
        }

        projects = projects.stream()
                .filter(project -> {
                    Long memberCount = getPersonnelCountByMajor(project, major);
                    Long currentMemberCount = redisService.getCurrentMemberCount(project.getId(), major);
                    return (memberCount - currentMemberCount > 0) ? true : false;
                })
                .collect(Collectors.toList());

        return projects.stream()
                .map(project ->{
                    String redisKey = "project:" + project.getId();
                    Map<Object, Object> redisHash = redisTemplate.opsForHash().entries(redisKey);
                    return new FindMajorProjectResponseDto(project, redisHash);
                })
                .collect(Collectors.toList());
    }

    private Long getPersonnelCountByMajor(ProjectDomain project, String major) {
        switch (major) {
            case "backend":
                return project.getBackend_personnel();
            case "frontend":
                return project.getFrontend_personnel();
            case "designer":
                return project.getDesigner_personnel();
            case "promoter":
                return project.getPromoter_personnel();
            default:
                throw new MajorNotFoundException("major not found", ErrorCode.MAJOR_NOTFOUND);
        }
    }
}
