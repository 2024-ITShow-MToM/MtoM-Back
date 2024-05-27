package com.MtoM.MtoM.domain.project;

import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.project.dto.res.FindProjectResponseDto;
import com.MtoM.MtoM.domain.project.dto.res.ListProjectResponseDto;
import com.MtoM.MtoM.domain.project.dto.req.RegisterProjectRequestDto;
import com.MtoM.MtoM.domain.project.service.ProjectService;
import com.MtoM.MtoM.global.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ResponseMessage<ProjectDomain>> registerProject(@ModelAttribute RegisterProjectRequestDto requestDto) throws IOException {
        ProjectDomain project =  projectService.registerProject(requestDto);
        return new ResponseEntity<>(new ResponseMessage<>("Project created successfully", project), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseMessage<List<ListProjectResponseDto>>> listProject(){
        List<ListProjectResponseDto> projects = projectService.listProject();
        return new ResponseEntity<>(new ResponseMessage<>("Projects retrieved successfully", projects), HttpStatus.OK);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ResponseMessage<FindProjectResponseDto>> findProject(@PathVariable("projectId") Long projectId){
        FindProjectResponseDto project = projectService.findProject(projectId);
        return new ResponseEntity<>(new ResponseMessage<>("prject reterieved successfully", project), HttpStatus.OK);
    }
}
