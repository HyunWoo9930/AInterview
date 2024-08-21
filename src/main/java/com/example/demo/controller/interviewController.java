package com.example.demo.controller;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.InterviewService;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/interview")
public class interviewController {

	public interviewController(InterviewService interviewService) {
		this.interviewService = interviewService;
	}

	private final InterviewService interviewService;

	@PostMapping(value = "/interview", consumes = "multipart/form-data")
	public ResponseEntity<?> getInterview(
		@Parameter(name = "file", description = "음성 데이터")
		@RequestParam(value = "wav file") MultipartFile file
	) {
		try {
			Path tempFile = Files.createTempFile("temp", ".wav");

			file.transferTo(tempFile.toFile());

			String result = interviewService.interview(tempFile.toString());

			return ResponseEntity.ok().body(result);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Failed to process the file");
		}
	}


}
