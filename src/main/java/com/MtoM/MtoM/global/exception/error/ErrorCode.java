package com.MtoM.MtoM.global.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INTER_SERER_ERROR(500, "SERVER-ERROR-500", "내부 서버 오류가 발생했습니다.");

    private int status;
    private String errorCode;
    private String errorMessage;
}
