package com.MtoM.MtoM.domain.user.service;

import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.dto.res.FindByUserResponseDto;
import com.MtoM.MtoM.domain.user.facade.UserFacade;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.exception.IDNotFoundException;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FindByUserService {
    private final UserRepository userRepository;
    private final UserFacade userFacade;

    @Transactional(readOnly = true)
    public FindByUserResponseDto execute(String id){
        userFacade.checkId(id);

        UserDomain user = userRepository.findById(id)
                .orElseThrow(() -> new IDNotFoundException("user not found", ErrorCode.ID_NOTFOUND));

        return new FindByUserResponseDto(user);
    }
}
