    package com.MtoM.MtoM.domain.mentor.dto.req;

    import com.MtoM.MtoM.domain.mentor.dto.Worry;
    import com.MtoM.MtoM.domain.user.domain.Major;
    import lombok.Getter;

    @Getter
    public class MentorRecommendRequest {
        private String userId;
        private Worry worry;
        private Major major;
        private String personal;
    }
