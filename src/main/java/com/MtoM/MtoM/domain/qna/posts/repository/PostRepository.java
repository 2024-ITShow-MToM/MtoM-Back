package com.MtoM.MtoM.domain.qna.posts.repository;

import com.MtoM.MtoM.domain.qna.posts.domain.PostDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostDomain, Long> {
    PostDomain findById(long id);

    List<PostDomain> findAllByUserId(String userId);
}
