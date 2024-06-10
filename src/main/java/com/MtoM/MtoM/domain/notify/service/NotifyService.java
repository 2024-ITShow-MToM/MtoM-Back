package com.MtoM.MtoM.domain.notify.service;

import com.MtoM.MtoM.domain.notify.domain.Notify;
import com.MtoM.MtoM.domain.notify.repository.NotifyRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotifyService {

    private final NotifyRepository notifyRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;
    private final ObjectMapper objectMapper;

    public NotifyService(NotifyRepository notifyRepository, RedisTemplate<String, Object> redisTemplate,
                         @Qualifier("notificationTopic") ChannelTopic topic, ObjectMapper objectMapper) {
        this.notifyRepository = notifyRepository;
        this.redisTemplate = redisTemplate;
        this.topic = topic;
        this.objectMapper = objectMapper;
    }

    public void saveNotification(Notify notify) {
        try {
            String notifyJson = objectMapper.writeValueAsString(notify);
            redisTemplate.convertAndSend(topic.getTopic(), notifyJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Notify savedNotify = notifyRepository.save(notify);
//        redisTemplate.convertAndSend(topic.getTopic(), savedNotify);
//        return savedNotify;
    }

    public List<Notify> getNotifications(UserDomain receiver) {
        return notifyRepository.findByReceiver(receiver);
    }
}