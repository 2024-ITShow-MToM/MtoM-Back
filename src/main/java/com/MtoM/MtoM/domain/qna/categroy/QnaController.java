package com.MtoM.MtoM.domain.qna.categroy;

import com.MtoM.MtoM.domain.qna.categroy.dao.QnaPostResponse;
import com.MtoM.MtoM.domain.qna.categroy.dao.QnaSelectResponse;
import com.MtoM.MtoM.domain.qna.categroy.service.QnaCategoryService;
import com.MtoM.MtoM.domain.qna.selects.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<QnaPostResponse> getQnaPosts(@RequestParam String sortBy) {
        switch (sortBy) {
            case "comments":
                return qnaCategoryService.getQnaPostsSortedByComments();
            case "hearts":
                return qnaCategoryService.getQnaPostsSortedByHearts();
            case "views":
                return qnaCategoryService.getQnaPostsSortedByViews();
            default:
                throw new IllegalArgumentException("Invalid sortBy parameter");
        }
    }

//    @GetMapping("/selects")
//    public List<QnaSelectResponse> getQnaSelects() {
//        return qnaCategoryService.getQnaSelects();
//    }

    @GetMapping("/result/{selectId}")
    public Map<String, Double> getVoteResult(@PathVariable Long selectId) {
        return voteService.getVotePercentages(selectId);
    }

}