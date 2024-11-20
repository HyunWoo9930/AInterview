package com.example.ainterview.controller;

import com.example.ainterview.dto.SignupRequest;
import com.example.ainterview.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<String> joinProcess(@RequestBody @Valid SignupRequest signupRequest) {
    if (signupRequest == null) {
      return ResponseEntity.badRequest().body("값을 입력해주세요.");
    }
    log.info("회원가입 email: {}", signupRequest.getEmail());
    userService.joinProcess(signupRequest);
    return ResponseEntity.ok("회원가입이 완료되었습니다.");
  }

//  @PostMapping("/login")
//  public ResponseEntity<String> loginProcess(@RequestBody @Valid LoginRequest loginRequest) {
//    if (loginRequest == null) {
//      return ResponseEntity.badRequest().body("값을 입력해주세요.");
//    }
//    log.info("로그인 email: {}", loginRequest.getEmail());
//    boolean isSuccess = userService.login(loginRequest);
//    if (isSuccess) {
//      return ResponseEntity.ok("로그인 성공");
//    } else {
//      return ResponseEntity.status(401).body("로그인 실패: 잘못된 이메일 또는 비밀번호입니다.");
//    }
//  }
}
