package com.MtoM.MtoM.domain.posts.repository;

import com.MtoM.MtoM.domain.posts.domain.PostCommentDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostCommentDomain, Long> {
    List<PostCommentDomain> findByPostId(Long postId);

    int countByPostId(Long id);
}