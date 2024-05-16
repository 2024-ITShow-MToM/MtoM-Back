package com.MtoM.MtoM.domain.qna.selects.controller;

import com.MtoM.MtoM.domain.qna.selects.domain.SelectDomain;
import com.MtoM.MtoM.domain.qna.selects.dto.CreateSelectDTO;
import com.MtoM.MtoM.domain.qna.selects.dto.SelectWithCountsDTO;
import com.MtoM.MtoM.domain.qna.selects.dto.VoteDTO;
import com.MtoM.MtoM.domain.qna.selects.service.SelectRedisService;
import com.MtoM.MtoM.domain.qna.selects.service.SelectService;
import com.MtoM.MtoM.global.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/selects")
public class SelectController {
    @Autowired
    private SelectService selectService;

    @Autowired
    private SelectRedisService redisService;

    @GetMapping
    public ResponseEntity<ResponseMessage<List<SelectDomain>>> getAllSelects() {
        List<SelectDomain> selects = selectService.getAllSelects();
        return new ResponseEntity<>(new ResponseMessage<>("Selects retrieved successfully", selects), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage<SelectWithCountsDTO>> getSelectById(@PathVariable Long id) {
        Optional<SelectDomain> select = selectService.getSelectById(id);
        if (select.isPresent()) {
            redisService.incrementViewCount(id);
            int viewCount = redisService.getViewCount(id);
            int heartCount = redisService.getPostHearts(id);
            SelectWithCountsDTO selectWithCounts = new SelectWithCountsDTO(select.get(), viewCount, heartCount);
            return new ResponseEntity<>(new ResponseMessage<>("Select retrieved successfully", selectWithCounts), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseMessage<>("Select not found", null), HttpStatus.NOT_FOUND);
        }
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

    @PostMapping("/{id}/heart")
    public ResponseEntity<String> toggleHeart(@PathVariable Long id, @RequestParam String userId) {
        redisService.togglePostHeart(userId, id);
        return ResponseEntity.ok("Heart toggled successfully");
    }
}