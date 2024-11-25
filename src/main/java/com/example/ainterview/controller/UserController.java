package com.example.ainterview.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ainterview.domain.user.ResumeCreateResponse;
import com.example.ainterview.dto.SignupRequest;
import com.example.ainterview.dto.request.ApplicationRequest;
import com.example.ainterview.dto.response.ApplicationResponse;
import com.example.ainterview.service.ApplicationService;
import com.example.ainterview.service.UserService;
import com.example.ainterview.utils.GetUserByJWT;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;
	private final ApplicationService applicationService;
	private final GetUserByJWT getUserByJWT;

	@PostMapping("/signup")
	public ResponseEntity<String> joinProcess(@RequestBody @Valid SignupRequest signupRequest) {
		log.info("회원가입 email: {}", signupRequest.getEmail());
		userService.joinProcess(signupRequest);
		return ResponseEntity.ok("회원가입이 완료되었습니다.");
	}

	@PostMapping("/application")
	public ResponseEntity<String> saveApplication(@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody ApplicationRequest request) {
		try {
			return ResponseEntity.ok(applicationService.saveApplication(userDetails, request));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping("/application")
	public ResponseEntity<ApplicationResponse> getApp(@RequestParam(value = "applicationId") Long id) {
		try {
			return ResponseEntity.ok(applicationService.getApplication(id));
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/resume")
	public ResponseEntity<?> saveResume(@AuthenticationPrincipal UserDetails userDetails, @RequestBody
	ResumeCreateResponse resumeCreateResponse) {
		applicationService.saveResume(userDetails, resumeCreateResponse);
		return ResponseEntity.ok("success");
	}
}
