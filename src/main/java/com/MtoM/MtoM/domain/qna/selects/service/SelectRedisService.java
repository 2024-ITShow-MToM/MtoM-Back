package com.MtoM.MtoM.domain.qna.selects.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SelectRedisService {
    private static final String VIEW_COUNT_KEY_PREFIX = "viewCount::";
    private static final String HEART_COUNT_KEY_PREFIX = "heartCount::";

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplateForHash;

    public void incrementViewCount(Long selectId) {
        String key = VIEW_COUNT_KEY_PREFIX + selectId;
        redisTemplate.opsForValue().increment(key, 1);
        redisTemplate.expire(key, 1, TimeUnit.DAYS);
    }

    public int getViewCount(Long selectId) {
        String key = VIEW_COUNT_KEY_PREFIX + selectId;
        Integer count = redisTemplate.opsForValue().get(key);
        return count != null ? count : 0;
    }

    public void togglePostHeart(String userId, Long selectId) {
        String key = "select:" + selectId + ":hearts";
        Boolean isHearted = redisTemplateForHash.opsForHash().hasKey(key, userId);
        if (isHearted != null && isHearted) {
            redisTemplateForHash.opsForHash().delete(key, userId);
        } else {
            redisTemplateForHash.opsForHash().put(key, userId, 1);
        }
    }

    public int getPostHearts(Long selectId) {
        String key = "select:" + selectId + ":hearts";
        return Math.toIntExact(redisTemplateForHash.opsForHash().size(key));
    }

    public boolean isPostHearted(String userId, Long selectId) {
        String key = "select:" + selectId + ":hearts";
        return redisTemplateForHash.opsForHash().hasKey(key, userId);
    }
}