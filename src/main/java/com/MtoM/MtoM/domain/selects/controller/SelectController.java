package com.MtoM.MtoM.domain.selects.controller;

import com.MtoM.MtoM.domain.selects.domain.SelectDomain;
import com.MtoM.MtoM.domain.selects.dto.CreateSelectDTO;
import com.MtoM.MtoM.domain.selects.service.SelectService;
import com.MtoM.MtoM.domain.selects.service.VoteService;
import com.MtoM.MtoM.global.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/selects")
@RequiredArgsConstructor
public class SelectController {

    @Autowired
    private SelectService selectService;
    @Autowired
    private final VoteService voteService;


    @GetMapping
    public ResponseEntity<ResponseMessage<List<SelectDomain>>> getAllSelects() {
        List<SelectDomain> selects = selectService.getAllSelects();
        return new ResponseEntity<>(new ResponseMessage<>("Selects retrieved successfully", selects), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ResponseMessage<SelectDomain>> createSelect(@RequestBody CreateSelectDTO select) {
        SelectDomain createdSelect = selectService.createSelect(select);
        return new ResponseEntity<>(new ResponseMessage<>("Select created successfully", createdSelect), HttpStatus.CREATED);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ResponseMessage<Void>> updateSelect(@PathVariable Long id, @RequestBody CreateSelectDTO selectDTO, @RequestParam String userId) {
        try {
            selectService.updateSelect(id, selectDTO, userId);
            return new ResponseEntity<>(new ResponseMessage<>("Select updated successfully", null), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseMessage<>(e.getMessage(), null), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<Void>> deleteSelect(@PathVariable Long id, @RequestParam String userId) {
        try {
            selectService.deleteSelect(id, userId);
            return new ResponseEntity<>(new ResponseMessage<>("Select deleted successfully", null), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseMessage<>(e.getMessage(), null), HttpStatus.UNAUTHORIZED);
        }
    }


    // 사용자 투표를 처리하는 엔드포인트
    @PostMapping("/{selectId}/{option}")
    public void vote(@PathVariable Long selectId, @PathVariable String option, @RequestParam String userId) {
        voteService.vote(selectId, option, userId);
    }

    // 투표 결과를 조회하는 엔드포인트
    @GetMapping("/{selectId}/results")
    public Map<Object, Object> getVoteResults(@PathVariable Long selectId) {
        return voteService.getVoteResult(selectId);
    }

}