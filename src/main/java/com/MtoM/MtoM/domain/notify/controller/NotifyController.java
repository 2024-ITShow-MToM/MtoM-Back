package com.MtoM.MtoM.domain.notify.controller;

import com.MtoM.MtoM.domain.notify.domain.Notify;
import com.MtoM.MtoM.domain.notify.service.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotifyController {

    private final Sinks.Many<Notify> sink;
    private final NotifyService notifyService;

    @GetMapping("/notifications")
    public Flux<ServerSentEvent<Notify>> getNotifications(@RequestParam String userId) {
        return sink.asFlux()
                .filter(notify -> notify.getReceiver().getId().equals(userId))
                .map(notify -> ServerSentEvent.builder(notify).build())
                .delayElements(Duration.ofSeconds(1));
    }
}