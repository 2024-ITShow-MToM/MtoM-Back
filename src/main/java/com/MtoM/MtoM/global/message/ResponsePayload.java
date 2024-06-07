package com.MtoM.MtoM.global.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponsePayload<T> {
    private String message;
    private T data;
}