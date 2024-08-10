package com.sparta.msa_exam.auth.controller;

import com.sparta.msa_exam.auth.dto.SignupRequestDto;
import com.sparta.msa_exam.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Long> signupUser(@RequestBody SignupRequestDto requestDto) {
        Long userId = authService.signupUser(requestDto.getLoginId(), requestDto.getPassword());
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

}
