package com.MtoM.MtoM.domain.groupChat.repository;

import com.MtoM.MtoM.domain.groupChat.domain.GroupChatContentDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupChatContentRepository extends JpaRepository<GroupChatContentDomain, Long> {
}