package com.MtoM.MtoM.domain.qna.categroy;

import com.MtoM.MtoM.domain.qna.categroy.dao.QnaPostResponse;
import com.MtoM.MtoM.domain.qna.categroy.dao.QnaSelectResponse;
import com.MtoM.MtoM.domain.qna.categroy.service.QnaCategoryService;
import com.MtoM.MtoM.domain.qna.selects.service.VoteService;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class QnaController {

    @Autowired
    private QnaCategoryService qnaCategoryService;
    @Autowired
    private VoteService voteService;

//    @GetMapping("/posts")
//    public List<QnaPostResponse> getQnaPosts() {
//        return qnaCategoryService.getQnaPosts();
//    }

    @GetMapping("/posts")
    public List<QnaPostResponse> getQnaPosts(@RequestParam(defaultValue = "latest") String sortBy) {
        switch (sortBy) {
            case "comments":
                return qnaCategoryService.getQnaPostsSortedByComments();
            case "hearts":
                return qnaCategoryService.getQnaPostsSortedByHearts();
            case "views":
                return qnaCategoryService.getQnaPostsSortedByViews();
            case "latest":
                return qnaCategoryService.getQnaPostsSortedByCreatedAt();
            default:
                throw new IllegalArgumentException("Invalid sortBy parameter");
        }
    }


    @GetMapping("/selects")
    public List<QnaSelectResponse> getAllQnaSelectResponses(@RequestParam String userId) {
        return qnaCategoryService.getAllQnaSelectResponses(userId);
    }


    @GetMapping("/result/{selectId}")
    public Map<String, Double> getVoteResult(@PathVariable Long selectId) {
        return voteService.getVotePercentages(selectId);
    }
    @GetMapping
    public List<Object> getQnaPostsAndSelectsSortedByCreatedAt(
            @RequestParam String userId,
            @RequestParam(required = false) String keyword) {
        return qnaCategoryService.getQnaPostsAndSelectsSortedByCreatedAt(userId,keyword);
    }

}