package com.example.ainterview.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.example.ainterview.dto.request.InterviewRequest;
import com.example.ainterview.dto.response.InterviewResponse;
import com.example.ainterview.service.PdfService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.ainterview.service.InterviewService;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/interview")
public class interviewController {

	public interviewController(InterviewService interviewService, PdfService pdfService) {
		this.interviewService = interviewService;
        this.pdfService = pdfService;
    }

	private final InterviewService interviewService;
	private final PdfService pdfService;

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
		@RequestParam(value = "wav file") MultipartFile file,
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestParam(value = "interviewId") Long id) {
		try {
			Path tempFile = Files.createTempFile("temp", ".wav");

			file.transferTo(tempFile.toFile());

			String result = interviewService.interview(tempFile.toString(), id);

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

	@PostMapping("")
	public ResponseEntity<InterviewResponse> createInterview(@AuthenticationPrincipal UserDetails userDetails,
															 @RequestBody InterviewRequest request) {

		try {
			return ResponseEntity.ok(interviewService.createInterview(userDetails, request));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/script")
	public ResponseEntity<Resource> downloadScript(@RequestParam(value = "interviewId") Long id,
												   @RequestParam(defaultValue = "txt") String format) {
		try {
			Path scriptPath = interviewService.generateScript(id);

			Path filePath;
			if ("pdf".equalsIgnoreCase(format)) {
				filePath = pdfService.convertTextToPdf(scriptPath);
			} else {
				filePath = scriptPath;
			}

			Resource resource = new UrlResource(filePath.toUri());

			if (!resource.exists()) {
				return ResponseEntity.notFound().build();
			}

			return ResponseEntity.ok()
					.contentType("pdf".equalsIgnoreCase(format) ? MediaType.APPLICATION_PDF : MediaType.TEXT_PLAIN)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filePath.getFileName())
					.body(resource);

		} catch (IOException e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}
