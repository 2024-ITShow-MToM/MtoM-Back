package com.MtoM.MtoM.domain.user.service;

import com.MtoM.MtoM.domain.user.dto.RegisterRequestDto;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public void registerUser(RegisterRequestDto requestDto){
        userRepository.save(requestDto.toEntity());
    }
}
