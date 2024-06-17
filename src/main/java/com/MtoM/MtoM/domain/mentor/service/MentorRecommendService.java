package com.MtoM.MtoM.domain.mentor.service;

import com.MtoM.MtoM.domain.mentor.dto.req.MentorRecommendRequest;
import com.MtoM.MtoM.domain.mentor.dto.res.MentorRecommendResponse;
import com.MtoM.MtoM.domain.user.domain.Major;
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

        Set<UserDomain> uniqueUsers = new HashSet<>();
        uniqueUsers.addAll(usersByMajor);

        for (String content : personalContents) {
            List<UserDomain> usersByPersonal = userRepository.findAllByPersonalContent(content);
            uniqueUsers.addAll(usersByPersonal);
        }

        // 자신은 response에서 제외시키기
        Iterator<UserDomain> iterator = uniqueUsers.iterator();
        while (iterator.hasNext()) {
            UserDomain user = iterator.next();
            if (user.getId().equals(userId)) {
                iterator.remove();
            }
        }

        List<UserDomain> additionalUsers = userRepository.findRandomUsers(3 - uniqueUsers.size());
        uniqueUsers.addAll(additionalUsers);

        return uniqueUsers.stream()
                .limit(3)
                .map(MentorRecommendResponse::new)
                .collect(Collectors.toList());
    }

}