package com.MtoM.MtoM.domain.mentor.service;

import com.MtoM.MtoM.domain.mentor.domain.MentorDomain;
import com.MtoM.MtoM.domain.mentor.dto.req.MentorMatchingRequestDto;
import com.MtoM.MtoM.domain.mentor.repository.MentorRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.exception.IDNotFoundException;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MentorService {
    private final MentorRepository mentorRepository;
    private final UserRepository userRepository;
    public MentorDomain mentorMatching(MentorMatchingRequestDto requestDto){
        String mentorId = requestDto.getMentorId();
        String menteeId = requestDto.getMenteeId();

        UserDomain mentor = userRepository.findById(mentorId)
                        .orElseThrow(() -> new IDNotFoundException("mentor not found", ErrorCode.ID_NOTFOUND));
        UserDomain mentee = userRepository.findById(menteeId)
                        .orElseThrow(() -> new IDNotFoundException("mentee not found", ErrorCode.ID_NOTFOUND));

        return mentorRepository.save(requestDto.toEntity(mentor, mentee));
    }
}
