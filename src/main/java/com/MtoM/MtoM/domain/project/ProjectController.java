package com.MtoM.MtoM.domain.project;

import com.MtoM.MtoM.domain.project.domain.MatchingProjectDomain;
import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.project.dto.req.ApplicationProjectRequestDto;
import com.MtoM.MtoM.domain.project.dto.res.FindMajorProjectResponseDto;
import com.MtoM.MtoM.domain.project.dto.res.FindProjectResponseDto;
import com.MtoM.MtoM.domain.project.dto.res.ListProjectResponseDto;
import com.MtoM.MtoM.domain.project.dto.req.RegisterProjectRequestDto;
import com.MtoM.MtoM.domain.project.service.ProjectService;
import com.MtoM.MtoM.global.message.ResponsePayload;
import com.MtoM.MtoM.global.message.ResponsePayload;
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
    public ResponseEntity<ResponsePayload<ProjectDomain>> registerProject(@ModelAttribute RegisterProjectRequestDto requestDto) throws IOException {
        ProjectDomain project =  projectService.registerProject(requestDto);
        return new ResponseEntity<>(new ResponsePayload<>("Project created successfully", project), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponsePayload<List<ListProjectResponseDto>>> listProject(){
        List<ListProjectResponseDto> projects = projectService.listProject();
        return new ResponseEntity<>(new ResponsePayload<>("Projects retrieved successfully", projects), HttpStatus.OK);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ResponsePayload<FindProjectResponseDto>> findProject(@PathVariable("projectId") Long projectId){
        FindProjectResponseDto project = projectService.findProject(projectId);
        return new ResponseEntity<>(new ResponsePayload<>("prject reterieved successfully", project), HttpStatus.OK);
    }

    @PostMapping("/application")
    public ResponseEntity<ResponsePayload<MatchingProjectDomain>> applicationProject(@RequestBody ApplicationProjectRequestDto requestDto){
        MatchingProjectDomain application = projectService.applicationProject(requestDto);
        return new ResponseEntity<>(new ResponsePayload<>("prject matching successfully", application), HttpStatus.CREATED);
    }

    @GetMapping("/major/{major}")
    public ResponseEntity<ResponsePayload<List<FindMajorProjectResponseDto>>> majorProject(@PathVariable("major") String major){
        List<FindMajorProjectResponseDto> projects = projectService.findMajorProject(major);
        return new ResponseEntity<>(new ResponsePayload<>("project reterieved successfully", projects), HttpStatus.OK);
    }
}
