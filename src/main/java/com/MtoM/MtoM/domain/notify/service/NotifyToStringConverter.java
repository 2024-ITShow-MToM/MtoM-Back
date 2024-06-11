package com.MtoM.MtoM.domain.notify.service;

import com.MtoM.MtoM.domain.notify.domain.Notify;
import org.springframework.core.convert.converter.Converter;
import com.fasterxml.jackson.databind.ObjectMapper;
public class NotifyToStringConverter implements Converter<Notify, String> {

    private final ObjectMapper objectMapper;

    public NotifyToStringConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convert(Notify notify) {
        try {
            return objectMapper.writeValueAsString(notify);
        } catch (Exception e) {
            throw new RuntimeException("Error converting Notify to String", e);
        }
    }
}