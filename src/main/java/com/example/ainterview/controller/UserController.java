package com.example.ainterview.controller;

import java.util.List;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ainterview.domain.user.ResumeCreateResponse;
import com.example.ainterview.dto.CustomUserDetails;
import com.example.ainterview.dto.SignupRequest;
import com.example.ainterview.dto.UserRequest;
import com.example.ainterview.dto.UserResponse;
import com.example.ainterview.dto.request.ApplicationRequest;
import com.example.ainterview.dto.response.ApplicationResponse;
import com.example.ainterview.dto.response.ResumeGetResponse;
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
@CrossOrigin(origins = "*")
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
	public ResponseEntity<?> saveApplication(@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody ApplicationRequest request) {
		try {
			Long id = applicationService.saveApplication(userDetails, request);
			JSONObject json = new JSONObject();
			json.put("applicationId", id);

			String jsonStr = json.toJSONString();
			return ResponseEntity.ok(jsonStr);
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

	@GetMapping("/applicationList")
	public ResponseEntity<List<ApplicationResponse>> getAppList(@AuthenticationPrincipal UserDetails userDetails) {
		try {
			return ResponseEntity.ok(applicationService.getAppList(userDetails));
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/application")
	public ResponseEntity<String> delApp(@RequestParam(value = "applicationId") Long id) {
		try {
			return ResponseEntity.ok(applicationService.deleteApp(id));
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/application/question")
	public ResponseEntity<String> delCustomQuestion(@RequestParam(value = "questionId") Long id) {
		try {
			return ResponseEntity.ok(applicationService.deleteCustomQuestion(id));
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/application")
	public ResponseEntity<?> updateApp(@RequestParam(value = "applicationId") Long id,
		@RequestBody ApplicationRequest request) {
		try {
			return ResponseEntity.ok(applicationService.updateApp(id, request));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PostMapping("/resume/save")
	public ResponseEntity<?> saveResume(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody ResumeCreateResponse resumeCreateResponse) {
		applicationService.saveResume(userDetails, resumeCreateResponse);
		return ResponseEntity.ok("success");
	}

	@GetMapping("/resume/get")
	public ResponseEntity<ResumeGetResponse> getResume(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(applicationService.getResume(userDetails));
	}

	@PutMapping
	public ResponseEntity<UserResponse> updateUser(
		@RequestBody @Valid UserRequest userRequest,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getId();
		UserResponse updatedUser = userService.updateUser(userId, userRequest);
		return ResponseEntity.ok(updatedUser);
	}

	@GetMapping
	public UserResponse getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getId();
		return userService.getUserInfo(userId);
	}

	@GetMapping("/email-check")
	public ResponseEntity<Boolean> checkEmailDuplicate(@RequestParam String email) {
		boolean isDuplicate = userService.isEmailDuplicate(email);
		return ResponseEntity.ok(isDuplicate);
	}
}
