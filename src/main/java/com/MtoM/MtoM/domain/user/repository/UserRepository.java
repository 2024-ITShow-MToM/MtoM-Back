package com.MtoM.MtoM.domain.user.repository;

import com.MtoM.MtoM.domain.user.domain.Major;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserDomain, String> {
    boolean existsByEmail(String email);
    @Query(value = "SELECT * FROM users u WHERE u.name LIKE %:searchResult% OR CAST(u.student_id AS CHAR) LIKE %:searchResult%", nativeQuery = true)
    List<UserDomain> searchUsers(@Param("searchResult") String searchResult);
    List<UserDomain> findAllByMajor(Major major);
    @Query("SELECT u FROM users u WHERE u.personal LIKE %:content%")
    List<UserDomain> findAllByPersonalContent(@Param("content") String content);

    @Query(value = "SELECT * FROM users ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<UserDomain> findRandomUsers(@Param("count") int count);
}
