package com.MtoM.MtoM.domain.user.repository;

import com.MtoM.MtoM.domain.user.domain.SkillDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<SkillDomain, Long> {
}
