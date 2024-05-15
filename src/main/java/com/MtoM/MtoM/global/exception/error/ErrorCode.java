package com.MtoM.MtoM.global.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INTER_SERER_ERROR(500, "SERVER-ERROR-500", "내부 서버 오류가 발생했습니다."),
    ID_DUPLICATION(400, "USER-ERROR-400", "이미 가입된 아이디 입니다"),
    EMAIL_DUPLICATION(400, "USER-ERROR-400", "이미 가입된 이메일 입니다"),
    EMAIL_NOTFOUND(404, "USER-ERROR-404", "존재하지 않은 이메일 입니다"),
    ID_NOTFOUND(404, "USER-ERROR-404", "가입되지 않은 아이디 입니다.");

    private int status;
    private String errorCode;
    private String errorMessage;
}
