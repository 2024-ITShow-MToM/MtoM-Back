package com.MtoM.MtoM.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;


    public Long getCurrentMemberCount(Long projectId, String major) {
        String key = "project:" + projectId;
        String field = "current" + capitalize(major) + "Personnel";
        return (Long) redisTemplate.opsForHash().get(key, field);
    }
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void addCurrentMemberCount(Long projectId, String role) {
        String redisKey = "project:" + projectId;
        String field = "current" + capitalize(role) + "Personnel";

        Object value = redisTemplate.opsForHash().get(redisKey, field);

        Long incrementValue = 1L;
        if (value == null || !(value instanceof Long)) {
            value = 0L;
        } else {
            incrementValue = (Long) value + 1;
        }
        redisTemplate.opsForHash().put(redisKey, field, incrementValue);
    }
}
