package com.MtoM.MtoM.domain.user.service;

import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.dto.req.LoginUserRequestDto;
import com.MtoM.MtoM.domain.user.facade.UserFacade;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.exception.IDNotFoundException;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final UserRepository userRepository;
    private final UserFacade userFacade;
    
    @Transactional(readOnly = true)
    public String execute(LoginUserRequestDto requestDto){
        String id = requestDto.getId();
        userFacade.checkId(id);

        String password = requestDto.getPassword();
        UserDomain user = userRepository.findById(id)
                .orElseThrow(() -> new IDNotFoundException("user not found", ErrorCode.ID_NOTFOUND));

        String hashedPasword = user.getPassword();
        userFacade.checkPassword(password, hashedPasword);

        return id;
    }
}
