package com.MtoM.MtoM.domain.qna.posts.service;

import com.MtoM.MtoM.domain.qna.posts.domain.PostCommentDomain;
import com.MtoM.MtoM.domain.qna.posts.repository.PostCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostCommentRedisService {

    // Redis 키 접두사 상수 선언
    private static final String HEART_COUNT_KEY_PREFIX = "post:comment:heart:";
    private final PostCommentRepository postCommentRepository;
    private final RedisTemplate<String,Integer> redisTemplate;

    // 게시물 댓글의 좋아요 상태를 토글
    public void togglePostCommentHeart(String userId, Long commentId) {
        Optional<PostCommentDomain> commentOpt = postCommentRepository.findById(commentId); // 댓글 작성자의 userId 가져오기

        if(!commentOpt.isPresent()) {
            throw new IllegalArgumentException("Invalid comment ID : " + commentId);
        }

        PostCommentDomain comment = commentOpt.get();
        String commentCreatorId = comment.getUser().getId(); // 댓글 작성자의 userId 가져오기

        String key = HEART_COUNT_KEY_PREFIX + commentId;
        Boolean ishHearted = redisTemplate.opsForHash().hasKey(key, userId);

        if(ishHearted != null && ishHearted) {
            redisTemplate.opsForHash().delete(key, userId); // 이미 좋아요를 눌렀다면 취소
        } else {
            redisTemplate.opsForHash().put(key,userId,1); // 좋아요를 누르지 않았더면 좋아요 1증가
        }
    }

    // 게시물의 총 좋아요 수를 반환
    public Integer getPostCommentHearts(Long commentId) {
        String key = HEART_COUNT_KEY_PREFIX + commentId;
        return Math.toIntExact(redisTemplate.opsForHash().size(key));
    }

    // 특정 게시물에 좋아요를 누른 사용자 ID 목록을 반환하는 메서드
    public Set<String> getPostHeartedUsers(Long postId) {
        String key = HEART_COUNT_KEY_PREFIX + postId;
        return redisTemplate.opsForHash().keys(key)
                .stream()
                .map(Object::toString) // Ensure all keys are converted to String
                .collect(Collectors.toSet());
    }

}
