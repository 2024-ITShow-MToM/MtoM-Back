package com.MtoM.MtoM.domain.project.controller;

import com.MtoM.MtoM.domain.project.dto.RegisterProjectRequestDto;
import com.MtoM.MtoM.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<String> registerProject(@RequestBody RegisterProjectRequestDto requestDto){
        projectService.registerProject(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }
}

