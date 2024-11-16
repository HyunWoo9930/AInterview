package com.example.ainterview.controller;

import com.example.ainterview.dto.SignupRequest;
import com.example.ainterview.service.UserService;
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
  public ResponseEntity<String> joinProcess(@RequestBody SignupRequest signupRequest) {
    log.info("회원가입 email: {}", signupRequest.getEmail());
    userService.joinProcess(signupRequest);
    return ResponseEntity.ok("회원가입이 완료되었습니다.");
  }
}
