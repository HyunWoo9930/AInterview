package com.example.ainterview.controller;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ainterview.service.InterviewService;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/interview")
public class interviewController {

	public interviewController(InterviewService interviewService) {
		this.interviewService = interviewService;
	}

	private final InterviewService interviewService;

	@PostMapping(value = "/Pronounce", consumes = "multipart/form-data")
	public ResponseEntity<?> PronounceCheck(
		@Parameter(name = "file", description = "음성 데이터")
		@RequestParam(value = "wav file") MultipartFile file
	) {
		try {
			Path tempFile = Files.createTempFile("temp", ".wav");

			file.transferTo(tempFile.toFile());

			String result = interviewService.pronunciationAssessmentContinuousWithFile(tempFile.toString());

			return ResponseEntity.ok().body(result);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Failed to process the file");
		}
	}

	@PostMapping(value = "/interview", consumes = "multipart/form-data")
	public ResponseEntity<?> getInterview(
		@Parameter(name = "file", description = "음성 데이터")
		@RequestParam(value = "wav file") MultipartFile file) {
		try {
			Path tempFile = Files.createTempFile("temp", ".wav");

			file.transferTo(tempFile.toFile());

			String result = interviewService.interview(tempFile.toString());

			byte[] audioData = interviewService.convertTextToSpeech(result);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "interview_response.wav");

			return ResponseEntity.ok()
				.headers(headers)
				.body(audioData);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Failed to process the file");
		}
	}

	@PostMapping(value = "/interview2", consumes = "multipart/form-data")
	public ResponseEntity<?> getInterview2(
		@Parameter(name = "file", description = "음성 데이터")
		@RequestParam(value = "wav file") MultipartFile file) {
		try {
			Path tempFile = Files.createTempFile("temp", ".wav");

			file.transferTo(tempFile.toFile());

			String result = interviewService.interview(tempFile.toString());

			byte[] audioData = interviewService.convertTextToSpeech(result);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "interview_response.wav");

			return ResponseEntity.ok()
				.headers(headers)
				.body(audioData);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Failed to process the file");
		}
	}

}
