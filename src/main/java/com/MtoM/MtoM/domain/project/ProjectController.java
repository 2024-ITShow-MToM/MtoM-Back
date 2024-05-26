package com.MtoM.MtoM.domain.project;

import com.MtoM.MtoM.domain.project.domain.ProjectRedisDomain;
import com.MtoM.MtoM.domain.project.dto.RegisterProjectRequestDto;
import com.MtoM.MtoM.domain.project.repository.ProjectRedisRepository;
import com.MtoM.MtoM.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectRedisRepository projectRedisRepository;

    @PostMapping
    public ResponseEntity<String> registerProject(@ModelAttribute RegisterProjectRequestDto requestDto) throws IOException {
        projectService.registerProject(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

}
