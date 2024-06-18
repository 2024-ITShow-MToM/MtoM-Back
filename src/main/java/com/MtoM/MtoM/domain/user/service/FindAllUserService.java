package com.MtoM.MtoM.domain.user.service;

import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.dto.res.FindAllUserResponseDto;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FindAllUserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<FindAllUserResponseDto> execute(){
        List<UserDomain> users = userRepository.findAll();


        return users.stream()
                .map(FindAllUserResponseDto::new)
                .collect(Collectors.toList());    }
}
