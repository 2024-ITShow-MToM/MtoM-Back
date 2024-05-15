package com.MtoM.MtoM.domain.qna.posts.repository;

import com.MtoM.MtoM.domain.qna.posts.domain.PostDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostDomain, Long> {
}