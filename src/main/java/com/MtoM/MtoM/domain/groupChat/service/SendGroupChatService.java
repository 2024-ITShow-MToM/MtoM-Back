package com.MtoM.MtoM.domain.groupChat.service;

import com.MtoM.MtoM.domain.groupChat.domain.GroupChatContentDomain;
import com.MtoM.MtoM.domain.groupChat.domain.GroupChatDomain;
import com.MtoM.MtoM.domain.groupChat.dto.req.SendChatRequestDto;
import com.MtoM.MtoM.domain.groupChat.repository.GroupChatContentRepository;
import com.MtoM.MtoM.domain.groupChat.repository.GroupChatRepository;
import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.project.repository.ProjectRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.exception.IDNotFoundException;
import com.MtoM.MtoM.global.exception.ProjectNotFoundException;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SendGroupChatService {
    private final UserRepository userRepository;
    private final GroupChatRepository groupChatRepository;
    private final GroupChatContentRepository groupChatContentRepository;
    public GroupChatContentDomain execute(SendChatRequestDto requestDto, String userId){
        Long  projectId = requestDto.getProjectId();
        UserDomain user = userRepository.findById(userId)
                .orElseThrow(() -> new IDNotFoundException("user not found", ErrorCode.ID_NOTFOUND));

        GroupChatDomain groupChat = groupChatRepository.findByProjectId(projectId);

        return groupChatContentRepository.save(requestDto.toEntity(groupChat, user));
    }
}
