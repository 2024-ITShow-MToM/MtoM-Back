package com.MtoM.MtoM.domain.notify.service;

import com.MtoM.MtoM.domain.notify.domain.Notify;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class NotificationRedisSubscriber {


    private final Sinks.Many<Notify> sink;

    public NotificationRedisSubscriber(Sinks.Many<Notify> sink) {
        this.sink = sink;
    }

    public void onMessage(Notify message, String pattern) {
        sink.tryEmitNext(message);
    }
}