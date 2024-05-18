package com.MtoM.MtoM.domain.qna.posts.repository;

import com.MtoM.MtoM.domain.qna.posts.domain.PostCommentDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostCommentDomain, Long> {
}