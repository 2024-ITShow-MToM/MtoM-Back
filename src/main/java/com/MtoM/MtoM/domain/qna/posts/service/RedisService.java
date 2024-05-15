package com.MtoM.MtoM.domain.qna.posts.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void incrementPostViews(String postId) { // postId를 문자열로 사용
        String key = "post:" + postId + ":views";
        redisTemplate.opsForValue().increment(key);
    }

    public Long getPostViews(String postId) { // postId를 문자열로 사용
        String key = "post:" + postId + ":views";
        Object views = redisTemplate.opsForValue().get(key);
        return views != null ? (Long) views : 0;
    }

    public void togglePostHeart(String userId, Long postId) { // userId를 문자열로 사용
        String key = "post:" + postId + ":hearts";
        String field = userId; // 이미 문자열이므로 변환 없이 사용
        Boolean isHearted = redisTemplate.opsForHash().hasKey(key, field);
        if (isHearted != null && isHearted) {
            redisTemplate.opsForHash().delete(key, field);
        } else {
            redisTemplate.opsForHash().put(key, field, true);
        }
    }

    public boolean isPostHearted(String userId, Long postId) { // userId를 문자열로 사용
        String key = "post:" + postId + ":hearts";
        String field = userId; // 이미 문자열이므로 변환 없이 사용
        return redisTemplate.opsForHash().hasKey(key, field) != null && redisTemplate.opsForHash().hasKey(key, field);
    }
}
