package com.MtoM.MtoM.domain.project.repository;

import com.MtoM.MtoM.domain.groupChat.service.GroupChartList;
import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectDomain, Long> {
    @Query("SELECT p FROM projects p WHERE p.backend_personnel  > 0")
    List<ProjectDomain> findByBackend();

    @Query("SELECT p FROM projects p WHERE p.designer_personnel > 0")
    List<ProjectDomain> findByDesigner();

    @Query("SELECT p FROM projects p WHERE p.frontend_personnel > 0")
    List<ProjectDomain> findByFrontend();

    @Query("SELECT p FROM projects p WHERE p.promoter_personnel > 0")
    List<ProjectDomain> findByPromoter();

    List<ProjectDomain> findAllByUserId(String userId);
}
