package com.MtoM.MtoM.global.conifg;

import com.MtoM.MtoM.domain.notify.domain.Notify;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class SinkConfig {

    @Bean
    public Sinks.Many<Notify> notifySink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }
}