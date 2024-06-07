package com.MtoM.MtoM.domain.selects.repository;

import com.MtoM.MtoM.domain.selects.domain.SelectDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectRepository extends JpaRepository<SelectDomain, Long> {
}
