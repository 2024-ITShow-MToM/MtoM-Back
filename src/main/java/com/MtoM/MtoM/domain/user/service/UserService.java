package com.MtoM.MtoM.domain.user.service;

import com.MtoM.MtoM.domain.user.dto.RegisterRequestDto;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import com.MtoM.MtoM.global.exception.error.IdDuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(RegisterRequestDto requestDto){
        String id = requestDto.getId();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();

        duplicateId(id);

        userRepository.save(requestDto.toEntity(password));
    }

    public void duplicateId(String id){
        if(userRepository.existsById(id)){
            throw new IdDuplicateException("id duplicated", ErrorCode.ID_DUPLICATION);
        }
    }
}
