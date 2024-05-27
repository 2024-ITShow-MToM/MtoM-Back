package com.MtoM.MtoM.domain.mentor;

import com.MtoM.MtoM.domain.mentor.domain.MentorDomain;
import com.MtoM.MtoM.domain.mentor.dto.req.MentorMatchingRequestDto;
import com.MtoM.MtoM.domain.mentor.service.MentorService;
import com.MtoM.MtoM.global.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mentors")
public class MentorController {
    private final MentorService mentorService;
    @PostMapping
    public ResponseEntity<ResponseMessage<MentorDomain>> mentorMatching(@RequestBody MentorMatchingRequestDto requestDto){
        MentorDomain mentor = mentorService.mentorMatching(requestDto);
        return new ResponseEntity<>(new ResponseMessage<>("mentor apply successfully", mentor), HttpStatus.CREATED);
    }
}
