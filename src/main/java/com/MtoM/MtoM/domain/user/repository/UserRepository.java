package com.MtoM.MtoM.domain.user.repository;

import com.MtoM.MtoM.domain.user.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDomain, String> {

}
