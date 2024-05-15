package com.MtoM.MtoM.domain.qna.post.repository;

import com.MtoM.MtoM.domain.qna.post.domain.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
}
