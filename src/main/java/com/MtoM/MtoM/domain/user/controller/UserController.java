package com.MtoM.MtoM.domain.user.controller;

import com.MtoM.MtoM.domain.user.dto.RegisterRequestDto;
import com.MtoM.MtoM.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDto requestDto){
        userService.registerUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입에 성공했습니다");
    }
}
