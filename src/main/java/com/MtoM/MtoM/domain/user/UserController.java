package com.MtoM.MtoM.domain.user;

import com.MtoM.MtoM.domain.qna.categroy.dao.QnaPostResponse;
import com.MtoM.MtoM.domain.qna.categroy.service.QnaCategoryService;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.dto.req.LoginUserRequestDto;
import com.MtoM.MtoM.domain.user.dto.req.RegisterProfileInfoDto;
import com.MtoM.MtoM.domain.user.dto.req.RegisterRequestDto;
import com.MtoM.MtoM.domain.user.dto.res.FindAllUserResponseDto;
import com.MtoM.MtoM.domain.user.dto.res.FindByUserResponseDto;
import com.MtoM.MtoM.domain.user.service.UserService;
import com.MtoM.MtoM.global.S3Service.S3Service;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    private final UserService userService;
    private final QnaCategoryService postService;
    private final S3Service s3UploadService;

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDto requestDto){
        userService.registerUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입에 성공했습니다");
    }

    @PostMapping("/profile/info")
    public ResponseEntity<String> registerProfileInfo(@RequestBody RegisterProfileInfoDto requestDto){
        userService.registerProfileInfo(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("프로필 정보 등록 성공했습니다.");
    }

    @PostMapping("/profile/img")
    public ResponseEntity<String> registerProfileImg (@RequestParam(value = "profile") MultipartFile file,
                                                      @RequestParam(value = "id") String id) {
        try{
            s3UploadService.uploadProfileFile(id, file, "profile");
            return ResponseEntity.ok().body("프로필 이미지 업로드에 성공했습니다");
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("프로필 이미지 업로드에 실패했습니다");
        }

    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginUserRequestDto requestDto){
        String id = userService.loginUser(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<FindByUserResponseDto> findByUser (@PathVariable("userId") String userId){
        FindByUserResponseDto user = userService.findByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/profile/img/{userId}")
    public ResponseEntity<String> findByProfileImg(@PathVariable("userId") String userId){
        String profileImgURL = s3UploadService.getImagePath(userId);
        return ResponseEntity.status(HttpStatus.OK).body(profileImgURL);
    }

    @GetMapping("/posts/{userId}")
    public List<QnaPostResponse>  getQnaPostsByUser(@PathVariable("userId") String userId) {
        return postService.getQnaPostsByUser(userId);
    }

    @GetMapping
    public ResponseEntity<List<FindAllUserResponseDto>> findAllUser(){
        List<FindAllUserResponseDto> users = userService.findAllUser();
        return ResponseEntity.ok().body(users);
    }
}
