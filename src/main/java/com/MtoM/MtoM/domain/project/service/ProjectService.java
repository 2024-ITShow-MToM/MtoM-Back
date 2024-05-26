package com.MtoM.MtoM.domain.project.service;

import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.project.domain.ProjectRedisDomain;
import com.MtoM.MtoM.domain.project.dto.RegisterProjectRequestDto;
import com.MtoM.MtoM.domain.project.repository.ProjectRedisRepository;
import com.MtoM.MtoM.domain.project.repository.ProjectRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.S3Service.S3Service;
import com.MtoM.MtoM.global.exception.IDNotFoundException;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectRedisRepository projectRedisRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public void registerProject(RegisterProjectRequestDto requestDto) throws IOException {
        System.out.println(requestDto.getTitle());
        System.out.println(requestDto.getImage());
        // 이미지 업로드
        String imageUrl = s3Service.uploadImage(requestDto.getImage(), "project");

        // MySQL에 데이터 저장
        UserDomain user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IDNotFoundException("id not found", ErrorCode.ID_NOTFOUND));
        ProjectDomain projectDomain = projectRepository.save(requestDto.toEntity(user, imageUrl));

        Long projectId = projectDomain.getId();
        // Redis에 프로젝트 인원 데이터 저장
        ProjectRedisDomain projectRedisDomain = requestDto.toRedis(projectId);
        projectRedisRepository.save(projectRedisDomain);

    }
}
