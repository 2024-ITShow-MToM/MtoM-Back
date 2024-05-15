package com.MtoM.MtoM.domain.user.controller;

import com.MtoM.MtoM.domain.user.dto.RegisterProfileInfoDto;
import com.MtoM.MtoM.domain.user.dto.RegisterRequestDto;
import com.MtoM.MtoM.domain.user.service.UserService;
import com.MtoM.MtoM.global.S3Service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
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
}
