package com.MtoM.MtoM.domain.project.repository;

import com.MtoM.MtoM.domain.project.domain.MatchingProjectDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingProjectRepository extends JpaRepository<MatchingProjectDomain, Long> {
    boolean existsByUserIdAndProjectId(String userId, Long projectId);
}
