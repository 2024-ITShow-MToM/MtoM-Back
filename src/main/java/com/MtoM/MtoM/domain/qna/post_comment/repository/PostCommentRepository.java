package com.MtoM.MtoM.domain.qna.post_comment.repository;

import com.MtoM.MtoM.domain.qna.post_comment.domain.PostCommentDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostCommentDomain, Long> {
}