package com.MtoM.MtoM.domain.mentor.repository;

import com.MtoM.MtoM.domain.mentor.domain.MentorDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorRepository extends JpaRepository<MentorDomain, Long> {
}
