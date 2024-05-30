package com.MtoM.MtoM.domain.project.service;

import com.MtoM.MtoM.domain.project.domain.MatchingProjectDomain;
import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.project.domain.ProjectRedisDomain;
import com.MtoM.MtoM.domain.project.dto.req.ApplicationProjectRequestDto;
import com.MtoM.MtoM.domain.project.dto.res.FindMajorProjectResponseDto;
import com.MtoM.MtoM.domain.project.dto.res.FindProjectResponseDto;
import com.MtoM.MtoM.domain.project.dto.res.ListProjectResponseDto;
import com.MtoM.MtoM.domain.project.dto.req.RegisterProjectRequestDto;
import com.MtoM.MtoM.domain.project.repository.MatchingProjectRepository;
import com.MtoM.MtoM.domain.project.repository.ProjectRedisRepository;
import com.MtoM.MtoM.domain.project.repository.ProjectRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.S3Service.S3Service;
import com.MtoM.MtoM.global.exception.IDNotFoundException;
import com.MtoM.MtoM.global.exception.MajorNotFoundException;
import com.MtoM.MtoM.global.exception.ProjectAlreadyMatchException;
import com.MtoM.MtoM.global.exception.ProjectNotFoundException;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import com.MtoM.MtoM.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectRedisRepository projectRedisRepository;
    private final MatchingProjectRepository matchingProjectRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisService redisService;

    public ProjectDomain registerProject(RegisterProjectRequestDto requestDto) throws IOException {
        // 이미지 업로드
        String imageUrl = s3Service.uploadImage(requestDto.getImage(), "project");

        // MySQL에 데이터 저장
        UserDomain user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IDNotFoundException("id not found", ErrorCode.ID_NOTFOUND));
        ProjectDomain projectDomain = projectRepository.save(requestDto.toEntity(user, imageUrl));

        Long projectId = projectDomain.getId();

        // Redis에 프로젝트 인원 데이터 저장
        saveProjectRedisData(projectId, requestDto);

        return projectDomain;
    }
    private void saveProjectRedisData(Long projectId, RegisterProjectRequestDto requestDto) {
        String key = "project:" + projectId;

        Map<String, Long> projectData = new HashMap<>();
        projectData.put("frontendPersonnel", requestDto.getFrontend_personnel());
        projectData.put("backendPersonnel", requestDto.getBackend_personnel());
        projectData.put("designerPersonnel", requestDto.getDesigner_personnel());
        projectData.put("promoterPersonnel", requestDto.getPromoter_personnel());
        projectData.put("currentFrontendPersonnel", 0L);
        projectData.put("currentBackendPersonnel", 0L);
        projectData.put("currentDesignerPersonnel", 0L);
        projectData.put("currentPromoterPersonnel", 0L);

        redisTemplate.opsForHash().putAll(key, projectData);

    }

    @Transactional(readOnly = true)
    public List<ListProjectResponseDto> listProject(){
        List<ProjectDomain> projects = projectRepository.findAll();
        return projects.stream()
                .map(project ->{
                    String redisKey = "project:" + project.getId();
                    Map<Object, Object> redisHash = redisTemplate.opsForHash().entries(redisKey);
                    return new ListProjectResponseDto(project, redisHash);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FindProjectResponseDto findProject(Long projectId){
        ProjectDomain project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("project not found", ErrorCode.PROJECT_NOTFOUND));

        String redisKey = "project:" + project.getId();
        Map<Object, Object> redisHash = redisTemplate.opsForHash().entries(redisKey);

        return new FindProjectResponseDto(project, redisHash);
    }

    public MatchingProjectDomain applicationProject(ApplicationProjectRequestDto requestDto){
        String userId = requestDto.getUserId();
        Long projectId = requestDto.getProjectId();

        boolean exists = matchingProjectRepository.existsByUserIdAndProjectId(userId, projectId);
        if(exists)
            throw new ProjectAlreadyMatchException("project already match", ErrorCode.PROJECT_ALREADY_MATCH);

        UserDomain user = userRepository.findById(userId)
                .orElseThrow(() -> new IDNotFoundException("user not found", ErrorCode.ID_NOTFOUND));
        ProjectDomain project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("project not found", ErrorCode.PROJECT_NOTFOUND));

        //Todo:프로젝트 신청가능한 인원수 감소하기

        return matchingProjectRepository.save(requestDto.toEntity(user, project));
    }

    public List<FindMajorProjectResponseDto> findMajorProject(String major){
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
