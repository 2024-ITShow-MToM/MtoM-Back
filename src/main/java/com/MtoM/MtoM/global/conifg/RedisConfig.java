package com.MtoM.MtoM.global.conifg;

import com.MtoM.MtoM.domain.chat.service.RedisMessageSubscriber;
import com.MtoM.MtoM.domain.notify.service.NotificationRedisSubscriber;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, Integer> redisTemplate() {
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Integer.class)); // 값 타입을 Integer로 설정
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(Integer.class)); // 해시 값 타입을 Integer로 설정
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplateForObject(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                        @Qualifier("chatListenerAdapter") MessageListenerAdapter chatListenerAdapter,
                                                        @Qualifier("notificationListenerAdapter") MessageListenerAdapter notificationListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(chatListenerAdapter, chatTopic());
        container.addMessageListener(notificationListenerAdapter, notificationTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter chatListenerAdapter(RedisMessageSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }

    @Bean
    public MessageListenerAdapter notificationListenerAdapter(NotificationRedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }

    @Bean(name = "chatTopic")
    public ChannelTopic chatTopic() {
        return new ChannelTopic("chat");
    }

    @Bean(name = "notificationTopic")
    public ChannelTopic notificationTopic() {
        return new ChannelTopic("notificationChannel");
    }
}
