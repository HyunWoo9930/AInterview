package com.example.ainterview.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.example.ainterview.domain.user.Application;
import com.example.ainterview.domain.user.Questions;
import com.example.ainterview.domain.user.Resume;
import com.example.ainterview.domain.user.User;
import com.example.ainterview.dto.CustomUserDetails;
import com.example.ainterview.dto.QuestionsResponse;
import com.example.ainterview.dto.TodayQuestionDTO;
import com.example.ainterview.repository.ApplicationRepository;
import com.example.ainterview.repository.QuestionRepository;
import com.example.ainterview.repository.ResumeRepository;
import com.example.ainterview.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodayQuestionService {

	private final InterviewService interviewService;

	private final ApplicationRepository applicationRepository;
	private final ResumeRepository resumeRepository;
	private final UserRepository userRepository;
	private final QuestionRepository questionRepository;

	public List<QuestionsResponse> getTodayQuestion(UserDetails userDetails, Long applicationId) {
		Application referenceById = applicationRepository.getReferenceById(applicationId);
		User user = userRepository.findByEmail(userDetails.getUsername())
			.orElseThrow(() -> new RuntimeException("user 찾지 못했습니다."));
		Resume resume = resumeRepository.findByUser(user).orElseThrow(() -> new NotFoundException("이력서가 존재하지 않습니다."));

		String todayQuestionJson = interviewService.getTodayQuestion(resume.toString(), referenceById.toString());

		ObjectMapper objectMapper = new ObjectMapper();
		List<TodayQuestionDTO> todayQuestions;
		try {
			todayQuestions = objectMapper.readValue(todayQuestionJson, new TypeReference<>() {
			});
		} catch (JsonProcessingException e) {
			throw new RuntimeException("JSON 파싱에 실패했습니다.", e);
		}

		List<QuestionsResponse> list = new ArrayList<>();
		todayQuestions.forEach(
			question -> {
				Questions questions = questionRepository.save(
					Questions.builder()
						.question(question.getQuestion())
						.user(user)
						.createdAt(LocalDateTime.now())
						.build());

				list.add(QuestionsResponse.builder()
					.question(questions.getQuestion())
					.id(questions.getId())
					.createdAt(questions.getCreatedAt())
					.userId(questions.getUser().getId())
					.build());
			});
		return list;
	}

	public List<QuestionsResponse> getQuestionToday(CustomUserDetails customUserDetails) {
		User user = userRepository.findByEmail(customUserDetails.getUsername())
			.orElseThrow(() -> new RuntimeException("user 찾지 못했습니다."));

		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = LocalDateTime.now();

		return questionRepository.findAllByUserAndCreatedAtBetween(user, startOfDay,
			endOfDay).stream().map(questions -> QuestionsResponse.builder()
			.id(questions.getId())
			.userId(questions.getUser().getId())
			.question(questions.getQuestion())
			.createdAt(questions.getCreatedAt()).build()).toList();
	}

	public List<QuestionsResponse> getQuestionPast(CustomUserDetails customUserDetails) {
		User user = userRepository.findByEmail(customUserDetails.getUsername())
			.orElseThrow(() -> new RuntimeException("user 찾지 못했습니다."));

		// 오늘 시작 시간 계산
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();

		// 오늘 이전의 모든 질문 가져오기
		return questionRepository.findAllByUserAndCreatedAtBefore(user, startOfDay)
			.stream()
			.map(questions -> QuestionsResponse.builder()
				.id(questions.getId())
				.userId(questions.getUser().getId())
				.question(questions.getQuestion())
				.createdAt(questions.getCreatedAt())
				.build())
			.toList();
	}

}
