package com.MtoM.MtoM.global.conifg;
import com.MtoM.MtoM.domain.notify.service.NotifyToStringConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// CORS 설정을 위한 WebConfig 클래스
@RequiredArgsConstructor
@Configuration
@Service
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedHeaders("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS" , "PATCH")
                        .exposedHeaders("Authorization", "RefreshToken")
                        .allowCredentials(true);
            }
        };
    }

    private final ObjectMapper objectMapper;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new NotifyToStringConverter(objectMapper));
    }
}