package com.MtoM.MtoM.domain.project;

import com.MtoM.MtoM.domain.project.domain.MatchingProjectDomain;
import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.MtoM.MtoM.domain.project.dto.req.ApplicationProjectRequestDto;
import com.MtoM.MtoM.domain.project.dto.res.FindMajorProjectResponseDto;
import com.MtoM.MtoM.domain.project.dto.res.FindProjectResponseDto;
import com.MtoM.MtoM.domain.project.dto.res.ListProjectResponseDto;
import com.MtoM.MtoM.domain.project.dto.req.RegisterProjectRequestDto;
import com.MtoM.MtoM.domain.project.service.*;
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
    private final RegisterProjectService registerProjectService;
    private final ListProjectService listProjectService;
    private final FindProjectService findProjectService;
    private final ApplicationProjectService applicationProjectService;
    private final FindMajorProjectService findMajorProjectService;

    @PostMapping
    public ResponseEntity<ResponseMessage<ProjectDomain>> registerProject(@ModelAttribute RegisterProjectRequestDto requestDto) throws IOException {
        ProjectDomain project =  registerProjectService.execute(requestDto);
        return new ResponseEntity<>(new ResponseMessage<>("Project created successfully", project), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseMessage<List<ListProjectResponseDto>>> listProject(){
        List<ListProjectResponseDto> projects = listProjectService.execute();
        return new ResponseEntity<>(new ResponseMessage<>("Projects retrieved successfully", projects), HttpStatus.OK);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ResponseMessage<FindProjectResponseDto>> findProject(@PathVariable("projectId") Long projectId){
        FindProjectResponseDto project = findProjectService.execute(projectId);
        return new ResponseEntity<>(new ResponseMessage<>("prject reterieved successfully", project), HttpStatus.OK);
    }

    @PostMapping("/application")
    public ResponseEntity<ResponseMessage<MatchingProjectDomain>> applicationProject(@RequestBody ApplicationProjectRequestDto requestDto){
        MatchingProjectDomain application = applicationProjectService.execute(requestDto);
        return new ResponseEntity<>(new ResponseMessage<>("prject matching successfully", application), HttpStatus.CREATED);
    }

    @GetMapping("/major/{major}")
    public ResponseEntity<ResponseMessage<List<FindMajorProjectResponseDto>>> majorProject(@PathVariable("major") String major){
        List<FindMajorProjectResponseDto> projects = findMajorProjectService.execute(major);
        return new ResponseEntity<>(new ResponseMessage<>("project reterieved successfully", projects), HttpStatus.OK);
    }

}
