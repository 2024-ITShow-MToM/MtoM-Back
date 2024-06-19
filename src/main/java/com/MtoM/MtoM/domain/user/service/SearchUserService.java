package com.MtoM.MtoM.domain.user.service;

import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.dto.res.SearchUserResponseDto;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchUserService {
    private final UserRepository userRepository;

    public List<SearchUserResponseDto> execute(String searchResult){
        List<UserDomain> users = userRepository.searchUsers(searchResult);

        return users.stream()
                .map(SearchUserResponseDto::new)
                .collect(Collectors.toList());
    }
}
