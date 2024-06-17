    package com.MtoM.MtoM.domain.mentor.dto.req;

    import com.MtoM.MtoM.global.util.enums.Worry;
    import com.MtoM.MtoM.global.util.enums.Major;
    import lombok.Getter;

    @Getter
    public class MentorRecommendRequest {
        private String userId;
        private Worry worry;
        private Major major;
        private String personal;
    }
