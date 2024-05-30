package com.MtoM.MtoM.domain.project.service;

import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.project.dto.req.RegisterProjectRequestDto;
import com.MtoM.MtoM.domain.project.repository.ProjectRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.S3Service.S3Service;
import com.MtoM.MtoM.global.exception.IDNotFoundException;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class RegisterProjectService {
    private final ProjectRepository projectRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public ProjectDomain execute(RegisterProjectRequestDto requestDto) throws IOException {
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
}
