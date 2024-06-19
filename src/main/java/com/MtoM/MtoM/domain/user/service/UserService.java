package com.MtoM.MtoM.domain.user.service;

import com.MtoM.MtoM.domain.user.domain.SkillDomain;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.dto.req.LoginUserRequestDto;
import com.MtoM.MtoM.domain.user.dto.req.RegisterProfileInfoDto;
import com.MtoM.MtoM.domain.user.dto.req.RegisterRequestDto;
import com.MtoM.MtoM.domain.user.dto.res.FindAllUserResponseDto;
import com.MtoM.MtoM.domain.user.dto.res.FindByUserResponseDto;
import com.MtoM.MtoM.domain.user.dto.res.SearchUserResponseDto;
import com.MtoM.MtoM.domain.user.repository.SkillRepository;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.exception.*;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final PasswordEncoder passwordEncoder;
    

    @Transactional(readOnly = true)
    public List<SearchUserResponseDto> searchUser(String searchResult){
        List<UserDomain> users= userRepository.searchUsers(searchResult);

        return users.stream()
                .map(SearchUserResponseDto::new)
                .collect(Collectors.toList());
    }

}

