package com.MtoM.MtoM.domain.user;

import com.MtoM.MtoM.domain.qna.categroy.dao.QnaPostResponse;
import com.MtoM.MtoM.domain.user.dto.req.LoginUserRequestDto;
import com.MtoM.MtoM.domain.user.dto.req.RegisterProfileInfoDto;
import com.MtoM.MtoM.domain.user.dto.req.RegisterRequestDto;
import com.MtoM.MtoM.domain.user.dto.res.FindAllUserResponseDto;
import com.MtoM.MtoM.domain.user.dto.res.FindByUserResponseDto;
import com.MtoM.MtoM.domain.user.dto.res.SearchUserResponseDto;
import com.MtoM.MtoM.domain.user.service.*;
import com.MtoM.MtoM.global.S3Service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final RegisterUserService registerUserService;
    private final RegisterProfileService registerProfileService;
    private final RegisterProfileImageService registerProfileImageService;
    private final LoginService loginService;
    private final FindByUserService findByUserService;
    private final GetQnaPostByUserService getQnaPostByUserService;
    private final FindAllUserService findAllUserService;
    private final SearchUserService searchUserService;


    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDto requestDto){
        registerUserService.execute(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입에 성공했습니다");
    }

    @PostMapping("/profile/info")
    public ResponseEntity<String> registerProfileInfo(@RequestBody RegisterProfileInfoDto requestDto) throws IOException {
        registerProfileService.execute(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("프로필 등록 성공했습니다.");
    }

    @PostMapping("/profile/img")
    public ResponseEntity<String> registerProfileImg (@RequestParam(value = "profile") MultipartFile file,
                                                      @RequestParam(value = "id") String id) throws IOException {
        registerProfileImageService.execute(file, id);
        return ResponseEntity.status(HttpStatus.CREATED).body("프로필 이미지 등록 성공했습니다.");
    }
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginUserRequestDto requestDto){
        String id = loginService.execute(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<FindByUserResponseDto> findByUser (@PathVariable("userId") String userId){
        FindByUserResponseDto user = findByUserService.execute(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/posts/{userId}")
    public List<QnaPostResponse>  getQnaPostsByUser(@PathVariable("userId") String userId) {
        return getQnaPostByUserService.execute(userId);
    }

    @GetMapping
    public ResponseEntity<List<FindAllUserResponseDto>> findAllUser(){
        List<FindAllUserResponseDto> users = findAllUserService.execute();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/searches/{searchResult}")
    public ResponseEntity<List<SearchUserResponseDto>> searchUser(@PathVariable("searchResult") String searchResult){
        List<SearchUserResponseDto> users = searchUserService.execute(searchResult);
        return ResponseEntity.ok().body(users);
    }
}
