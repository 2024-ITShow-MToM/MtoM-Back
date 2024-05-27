package com.MtoM.MtoM.domain.mentor.dto.req;

import com.MtoM.MtoM.domain.mentor.domain.MentorDomain;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import lombok.Getter;
import org.springframework.security.core.parameters.P;

@Getter
public class MentorMatchingRequestDto {
    private String mentorId;
    private String menteeId;
    private boolean isMatching;
    private String application;

    public MentorDomain toEntity(UserDomain mentor, UserDomain mentee){
        return MentorDomain.builder()
                .mentor(mentor)
                .mentee(mentee)
                .is_matching(false)
                .application(application)
                .build();
    }
}
