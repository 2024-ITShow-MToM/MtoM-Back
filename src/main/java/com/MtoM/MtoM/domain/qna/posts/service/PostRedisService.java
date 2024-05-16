package com.MtoM.MtoM.domain.qna.posts.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostRedisService {

    private final RedisTemplate<String, Integer> redisTemplate; // RedisTemplate의 값 타입을 Integer로 변경

    public PostRedisService(RedisTemplate<String, Integer> redisTemplate) { // 생성자도 Integer로 변경
        this.redisTemplate = redisTemplate;
    }

    public void incrementPostViews(String postId) {
        String key = "post:" + postId + ":views";
        redisTemplate.opsForValue().increment(key); // Integer로 증가
    }

    public Integer getPostViews(String postId) {
        String key = "post:" + postId + ":views";
        Integer views = redisTemplate.opsForValue().get(key); // Integer로 받아옴
        return views != null ? views : 0; // null 처리도 Integer로
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
