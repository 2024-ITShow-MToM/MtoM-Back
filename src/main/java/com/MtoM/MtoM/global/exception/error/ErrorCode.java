package com.MtoM.MtoM.global.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INTER_SERER_ERROR(500, "SERVER-ERROR-500", "내부 서버 오류가 발생했습니다."),
    ID_DUPLICATION(400, "USER-ERROR-400", "이미 가입된 아이디 입니다");

    private int status;
    private String errorCode;
    private String errorMessage;
}
