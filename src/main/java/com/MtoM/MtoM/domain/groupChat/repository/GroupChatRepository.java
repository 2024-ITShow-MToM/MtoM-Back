package com.MtoM.MtoM.domain.groupChat.repository;

import com.MtoM.MtoM.domain.groupChat.domain.GroupChatDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupChatRepository extends JpaRepository<GroupChatDomain, Long> {
    boolean existsByProjectId(Long projectId);
}
