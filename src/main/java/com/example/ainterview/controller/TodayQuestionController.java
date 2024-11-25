package com.example.ainterview.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ainterview.dto.CustomUserDetails;
import com.example.ainterview.dto.QuestionsResponse;
import com.example.ainterview.service.TodayQuestionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class TodayQuestionController {
	private final TodayQuestionService todayQuestionService;

	@PostMapping("")
	public ResponseEntity<List<QuestionsResponse>> getTodayQuestion(
		@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam Long applicationId) {
		return ResponseEntity.ok(todayQuestionService.getTodayQuestion(customUserDetails, applicationId));
	}

	@GetMapping("/get/today")
	public ResponseEntity<List<QuestionsResponse>> getQuestionToday(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		return ResponseEntity.ok(todayQuestionService.getQuestionToday(customUserDetails));
	}

	@GetMapping("/get/past")
	public ResponseEntity<?> getQuestionPast(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		return ResponseEntity.ok(todayQuestionService.getQuestionPast(customUserDetails));
	}
}
