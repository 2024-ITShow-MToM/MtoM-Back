package com.MtoM.MtoM.domain.mentor.dto.res;

import com.MtoM.MtoM.global.util.enums.Major;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import lombok.Getter;

@Getter
public class MentorRecommendResponse {
    private String userId;
    private String name;
    private Major major;
    private String hashtag;
    private String introduction;
    private String profile;

    public MentorRecommendResponse(UserDomain user){
        this.userId = user.getId();
        this.name = user.getName();
        this.major = user.getMajor();
        this.hashtag = user.getMentoring_topics();
        this.introduction = user.getIntroduction();
        this.profile = user.getProfile();
    }
}
