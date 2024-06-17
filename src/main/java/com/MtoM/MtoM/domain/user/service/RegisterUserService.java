package com.MtoM.MtoM.domain.user.service;

import com.MtoM.MtoM.domain.user.dto.req.RegisterRequestDto;
import com.MtoM.MtoM.domain.user.facade.UserFacade;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RegisterUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFacade userFacade;

    @Transactional
    public void execute(RegisterRequestDto requestDto){
        String id = requestDto.getId();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();

        userFacade.duplicateId(id);
        userFacade.duplicatedEmail(email);

        userRepository.save(requestDto.toEntity(password));
    }
}
