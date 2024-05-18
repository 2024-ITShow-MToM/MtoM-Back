package com.MtoM.MtoM.domain.qna.posts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PostRedisService {

    // Redis 키 접두사 상수 선언
    private static final String VIEW_COUNT_KEY_PREFIX = "viewCount::";
    private static final String HEART_COUNT_KEY_PREFIX = "heartCount::";

    @Autowired
    private final RedisTemplate<String, Integer> redisTemplate; // RedisTemplate의 값 타입을 Integer로 변경

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


    public void togglePostHeart(String userId, Long postId) {
        String key = "post:" + postId + ":hearts";
        String field = userId;
        Boolean isHearted = redisTemplate.opsForHash().hasKey(key, field);
        if (isHearted != null && isHearted) {
            redisTemplate.opsForHash().delete(key, field);
        } else {
            redisTemplate.opsForHash().put(key, field, 1); // Integer로 저장
        }
    }

    public Integer getPostHearts(String postId) {
        String key = "post:" + postId + ":hearts";
        return Math.toIntExact(redisTemplate.opsForHash().size(key)); // Redis에 저장된 값이 Integer이므로 반환 타입도 Integer로 변경
    }

    public boolean isPostHearted(String userId, Long postId) {
        String key = "post:" + postId + ":hearts";
        String field = userId;
        redisTemplate.opsForHash().hasKey(key, field);
        return redisTemplate.opsForHash().hasKey(key, field); // 여기서는 Integer가 아니라 boolean으로 반환되므로 변경하지 않음
    }
}
