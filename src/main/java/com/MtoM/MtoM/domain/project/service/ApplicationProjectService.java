package com.MtoM.MtoM.domain.project.service;

import com.MtoM.MtoM.domain.project.domain.MatchingProjectDomain;
import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.project.dto.req.ApplicationProjectRequestDto;
import com.MtoM.MtoM.domain.project.repository.MatchingProjectRepository;
import com.MtoM.MtoM.domain.project.repository.ProjectRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.exception.IDNotFoundException;
import com.MtoM.MtoM.global.exception.ProjectAlreadyMatchException;
import com.MtoM.MtoM.global.exception.ProjectNotFoundException;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ApplicationProjectService {
    private final ProjectRepository projectRepository;
    private final MatchingProjectRepository matchingProjectRepository;
    private final UserRepository userRepository;

    public MatchingProjectDomain execute(ApplicationProjectRequestDto requestDto){
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
}
