package com.MtoM.MtoM.domain.user.repository;

import com.MtoM.MtoM.domain.user.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserDomain, String> {
    boolean existsByEmail(String email);
    @Query(value = "SELECT * FROM users u WHERE u.name LIKE %:searchResult% OR CAST(u.student_id AS CHAR) LIKE %:searchResult%", nativeQuery = true)
    List<UserDomain> searchUsers(@Param("searchResult") String searchResult);

}
