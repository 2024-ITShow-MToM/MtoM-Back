package com.MtoM.MtoM.domain.project.repository;

import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectDomain, Long> {
}

