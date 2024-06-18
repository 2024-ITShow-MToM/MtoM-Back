package com.MtoM.MtoM.domain.project.service;

import com.MtoM.MtoM.domain.groupChat.domain.GroupChatDomain;
import com.MtoM.MtoM.domain.groupChat.repository.GroupChatRepository;
import com.MtoM.MtoM.domain.project.domain.MatchingProjectDomain;
import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.project.dto.req.ApplicationProjectRequestDto;
import com.MtoM.MtoM.domain.project.repository.MatchingProjectRepository;
import com.MtoM.MtoM.domain.project.repository.ProjectRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
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

import java.util.Map;

@RequiredArgsConstructor
@Service
public class ApplicationProjectService {
    private final ProjectRepository projectRepository;
    private final MatchingProjectRepository matchingProjectRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final GroupChatRepository groupChatRepository   ;

    @Transactional
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

        String role = String.valueOf(requestDto.getRole());
        role = changeRoleName(role);
        Long currentMemberCount = redisService.getCurrentMemberCount(projectId, role);

        if (currentMemberCount == null || currentMemberCount <= 0) {
            throw new ProjectAlreadyMatchException("project already match", ErrorCode.PROJECT_ALREADY_MATCH);
        }

        redisService.addCurrentMemberCount(projectId, role);

        // 단체 채팅방 생성 혹은 추가
        if(!groupChatRepository.existsByProjectId(project.getId()))
            groupChatRepository.save(requestDto.chatToEntity(project));

        return matchingProjectRepository.save(requestDto.toEntity(user, project));
    }

    private String changeRoleName(String role){
        switch (role) {
            case "백엔드":
                return "backend";
            case "프론트엔드":
                return "frontend";
            case "디자인":
                return "designer";
            case "기획":
                return "promoter";
            default:
                return "";
        }
    }

}
