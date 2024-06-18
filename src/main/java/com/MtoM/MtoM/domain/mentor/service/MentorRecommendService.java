package com.MtoM.MtoM.domain.mentor.service;

import com.MtoM.MtoM.domain.mentor.dto.req.MentorRecommendRequest;
import com.MtoM.MtoM.domain.mentor.dto.res.MentorRecommendResponse;
import com.MtoM.MtoM.global.util.enums.Major;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MentorRecommendService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<MentorRecommendResponse> execute(MentorRecommendRequest requestDto){
        String userId = requestDto.getUserId();
        Major major = requestDto.getMajor();

        List<String> personalContents = Arrays.asList(requestDto.getPersonal().split(","))
                .stream().map(String::trim).collect(Collectors.toList());

        List<UserDomain> usersByMajor = userRepository.findAllByMajor(major);

        Set<UserDomain> uniqueUsers = new HashSet<>(usersByMajor);

        for (String content : personalContents) {
            List<UserDomain> usersByPersonal = userRepository.findAllByPersonalContent(content);
            uniqueUsers.addAll(usersByPersonal);
        }

        // 부족한 경우 임의의 사용자 추가
        if (uniqueUsers.size() < 3) {
            List<UserDomain> additionalUsers = userRepository.findRandomUsers(3 - uniqueUsers.size());
            uniqueUsers.addAll(additionalUsers);
        }

        return uniqueUsers.stream()
                .limit(3)
                .map(MentorRecommendResponse::new)
                .collect(Collectors.toList());
    }

}