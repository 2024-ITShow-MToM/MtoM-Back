package com.MtoM.MtoM.domain.user.dto.req;

import com.MtoM.MtoM.domain.user.domain.UserDomain;
import lombok.Getter;

@Getter
public class RegisterRequestDto {
    private String id;
    private String password;
    private String email;

    public UserDomain toEntity(String password){
        return UserDomain.builder()
                .id(id)
                .password(password)
                .email(email)
                .build();
    }
}
