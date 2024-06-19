package com.MtoM.MtoM.domain.mentor;

import com.MtoM.MtoM.domain.mentor.domain.MentorDomain;
import com.MtoM.MtoM.domain.mentor.dto.req.MentorMatchingRequestDto;
import com.MtoM.MtoM.domain.mentor.dto.req.MentorRecommendRequest;
import com.MtoM.MtoM.domain.mentor.dto.res.MentorRecommendResponse;
import com.MtoM.MtoM.domain.mentor.service.MentorRecommendService;
import com.MtoM.MtoM.domain.mentor.service.MentorService;
import com.MtoM.MtoM.global.message.ResponseMessage;
import com.MtoM.MtoM.global.message.ResponsePayload;
import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mentors")
public class MentorController {
    private final MentorService mentorService;
    private final MentorRecommendService mentorRecommendService;

    @PostMapping
    public ResponseEntity<ResponsePayload<MentorDomain>> mentorMatching(@RequestBody MentorMatchingRequestDto requestDto){
        MentorDomain mentor = mentorService.mentorMatching(requestDto);
        return new ResponseEntity<>(new ResponsePayload<>("mentor apply successfully", mentor), HttpStatus.CREATED);
    }

    @PostMapping("/recommends")
    public ResponseEntity<ResponsePayload<List<MentorRecommendResponse>>> recommendMentor(@RequestBody MentorRecommendRequest requestDto) {
        List<MentorRecommendResponse> mentorRecommends = mentorRecommendService.execute(requestDto);
        return new ResponseEntity<>(new ResponsePayload<>("mentor recommend successfully", mentorRecommends), HttpStatus.OK);
    }
}
