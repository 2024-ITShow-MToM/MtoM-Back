package com.MtoM.MtoM.domain.project.repository;

import com.MtoM.MtoM.domain.project.domain.ProjectRedisDomain;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRedisRepository extends CrudRepository<ProjectRedisDomain, Integer> {

}

