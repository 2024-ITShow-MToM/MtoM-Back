package com.MtoM.MtoM.domain.groupChat.service;

import com.MtoM.MtoM.domain.chat.dto.GroupChatListResponseDto;
import com.MtoM.MtoM.domain.groupChat.domain.GroupChatDomain;
import com.MtoM.MtoM.domain.groupChat.repository.GroupChatRepository;
import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GroupChartList {
    private final ProjectRepository projectRepository;
    private final GroupChatRepository groupChatRepository;

    public List<GroupChatListResponseDto> execute(String userId) {
        List<ProjectDomain> projects = projectRepository.findAllByUserId(userId);

        List<Long> projectIds = projects.stream()
                .map(ProjectDomain::getId)
                .collect(Collectors.toList());

        List<GroupChatDomain> groupChats = groupChatRepository.findAllByProjectIdIn(projectIds);

        return groupChats.stream()
                .map(GroupChatListResponseDto::new)
                .collect(Collectors.toList());
    }
}
