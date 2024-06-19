package com.MtoM.MtoM.domain.groupChat.repository;

import com.MtoM.MtoM.domain.groupChat.domain.GroupChatDomain;
import com.MtoM.MtoM.domain.groupChat.service.GroupChartList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupChatRepository extends JpaRepository<GroupChatDomain, Long> {
    boolean existsByProjectId(Long projectId);

    List<GroupChatDomain> findAllByProjectIdIn(List<Long> projectIds);
}
