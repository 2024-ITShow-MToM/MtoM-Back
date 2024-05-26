package com.MtoM.MtoM.domain.project;

import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.project.domain.ProjectRedisDomain;
import com.MtoM.MtoM.domain.project.dto.RegisterProjectRequestDto;
import com.MtoM.MtoM.domain.project.repository.ProjectRedisRepository;
import com.MtoM.MtoM.domain.project.service.ProjectService;
import com.MtoM.MtoM.global.ResponseMessage;
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
    public ResponseEntity<ResponseMessage<ProjectDomain>> registerProject(@ModelAttribute RegisterProjectRequestDto requestDto) throws IOException {
        ProjectDomain project =  projectService.registerProject(requestDto);
        return new ResponseEntity<>(new ResponseMessage<>("Project created successfully", project), HttpStatus.CREATED);
    }

}
