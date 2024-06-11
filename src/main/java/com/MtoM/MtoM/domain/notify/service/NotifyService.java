package com.MtoM.MtoM.domain.notify.service;

import com.MtoM.MtoM.domain.chat.domain.ChatMessage;
import com.MtoM.MtoM.domain.notify.domain.Notify;
import com.MtoM.MtoM.domain.notify.dto.NotifyResponseDTO;
import com.MtoM.MtoM.domain.notify.repository.NotifyRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotifyService {

    private final UserRepository userRepository;
    private final NotifyRepository notifyRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;
    private final ObjectMapper objectMapper;

    public NotifyService(UserRepository userRepository, NotifyRepository notifyRepository, RedisTemplate<String, Object> redisTemplate,
                         @Qualifier("notificationTopic") ChannelTopic topic, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.notifyRepository = notifyRepository;
        this.redisTemplate = redisTemplate;
        this.topic = topic;
        this.objectMapper = objectMapper;
    }

//    public void saveNotification(Notify notify) {
//        try {
//            String notifyJson = objectMapper.writeValueAsString(notify);
//            redisTemplate.convertAndSend(topic.getTopic(), notifyJson);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Notify savedNotify = notifyRepository.save(notify);
//        redisTemplate.convertAndSend(topic.getTopic(), savedNotify);
//        return savedNotify;
//    }

    public List<NotifyResponseDTO> getNotifications(String userId) {
        UserDomain receiver = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        List<Notify> notifications = notifyRepository.findByReceiver(receiver);

        // 엔티티 리스트를 DTO 리스트로 변환
        return notifications.stream()
                .map(notify -> new NotifyResponseDTO(
                        notify.getId(),
                        notify.getContentId(),
                        notify.getContent(),
                        notify.getIsRead(),
                        notify.getNotificationType().toString(),
                        notify.getSender().getId(),
                        notify.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public void saveNotification(Notify notify) {
        notifyRepository.save(notify);
    }
}