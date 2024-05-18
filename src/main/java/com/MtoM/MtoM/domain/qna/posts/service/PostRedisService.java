package com.MtoM.MtoM.domain.qna.posts.service;

import com.MtoM.MtoM.domain.qna.posts.domain.PostDomain;
import com.MtoM.MtoM.domain.qna.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PostRedisService {

    // Redis 키 접두사 상수 선언
    private static final String VIEW_COUNT_KEY_PREFIX = "post:view:";
    private static final String HEART_COUNT_KEY_PREFIX = "post:count:";

    private final RedisTemplate<String, Integer> redisTemplate; // RedisTemplate의 값 타입을 Integer로 변경
    private final PostRepository postRepository;

    // 게시물의 조회수를 증가시키는 메소드
    public void incrementViewCount(Long postId) {
        String key = VIEW_COUNT_KEY_PREFIX + postId; // Redis 키 생성
        redisTemplate.opsForValue().increment(key, 1);  // 조회수 1 증가
        redisTemplate.expire(key, 1, TimeUnit.DAYS); // 키의 유효기간을 1일로 설정
    }

    // 특정 게시물의 조회수를 반환하는 메소드
    public int getViewCount(Long postId) {
        String key = VIEW_COUNT_KEY_PREFIX + postId;
        Integer count = redisTemplate.opsForValue().get(key);
        return count != null ? count : 0; // 조회수가 null일 경우 0 반환
    }


    // 게시물의 좋아요 상태를 토글
    public void togglePostHeart(String userId, Long postId) {
        Optional<PostDomain> postOpt = postRepository.findById(postId);
        if (!postOpt.isPresent()) {
            throw new IllegalArgumentException("Invalid post ID: " + postId);
        }

        PostDomain post = postOpt.get();
        String postCreatorId = post.getUser().getId(); // 게시물 작성자의 userId 가져오기

        String key = HEART_COUNT_KEY_PREFIX + postId;
        Boolean isHearted = redisTemplate.opsForHash().hasKey(key, userId); // 해당 사용자가 이미 좋아요를 눌렀는지 확인
        if (isHearted != null && isHearted) {
            redisTemplate.opsForHash().delete(key, userId); // 이미 좋아요를 눌렀다면 좋아요 삭제
        } else {
            redisTemplate.opsForHash().put(key, userId, 1); // 좋아요를 누르지 않았다면 좋아요 추가
        }
    }


    // 게시물의 총 종하용 수를 반호나
    public Integer getPostHearts(Long postId) {
        String key = HEART_COUNT_KEY_PREFIX + postId;
        return Math.toIntExact(redisTemplate.opsForHash().size(key)); // 좋아요 수를 해시 크기로 반환
    }

    // 특정 사용자가 특정 게시물에 좋아요를 눌렀는지 확인
    public boolean isPostHearted(String userId, Long postId) {
        String key = HEART_COUNT_KEY_PREFIX + postId;
        redisTemplate.opsForHash().hasKey(key, userId); // Redis 키 생성
        return redisTemplate.opsForHash().hasKey(key, userId); // 해당 사용자가 좋아요를 눌렀는지 확인(눌렀다면 true, 그렇지 않다면 false 반환)
    }
}
