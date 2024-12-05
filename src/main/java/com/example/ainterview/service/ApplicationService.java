package com.example.ainterview.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ainterview.domain.user.Application;
import com.example.ainterview.domain.user.ApplicationCustom;
import com.example.ainterview.domain.user.Resume;
import com.example.ainterview.domain.user.ResumeCreateResponse;
import com.example.ainterview.domain.user.User;
import com.example.ainterview.dto.CustomUserDetails;
import com.example.ainterview.dto.request.ApplicationCustomRequest;
import com.example.ainterview.dto.request.ApplicationRequest;
import com.example.ainterview.dto.response.ApplicationResponse;
import com.example.ainterview.dto.response.ResumeGetResponse;
import com.example.ainterview.repository.ApplicationCustomRepository;
import com.example.ainterview.repository.ApplicationRepository;
import com.example.ainterview.repository.ResumeRepository;
import com.example.ainterview.repository.UserRepository;
import com.example.ainterview.utils.GetUserByJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

	private final UserRepository userRepository;
	private final ApplicationRepository applicationRepository;
	private final ApplicationCustomRepository applicationCustomRepository;
	private final CustomUserDetailsService customUserDetailsService;
	private final ResumeRepository resumeRepository;
	private final GetUserByJWT getUserByJWT;

	public Long saveApplication(UserDetails userDetails, ApplicationRequest request) {

		log.info("username {}", userDetails.getUsername());
		User user = userRepository.findByEmail(userDetails.getUsername())
			.orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

		Application application = Application.builder()
			.name(request.getName())
			.motivation(request.getMotivation())
			.teamwork(request.getTeamwork())
			.effort(request.getEffort())
			.aspiration(request.getAspiration())
			.user(user)
			.build();

		if (request.getCustomQuestions() != null) {
			for (ApplicationCustomRequest customRequest : request.getCustomQuestions()) {
				ApplicationCustom custom = ApplicationCustom.builder()
					.question(customRequest.getQuestion())
					.answer(customRequest.getAnswer())
					.application(application)
					.build();

				applicationCustomRepository.save(custom);
			}
		}

		applicationRepository.save(application);

		return application.getId();
	}

	public ApplicationResponse getApplication(Long id) {
		Application app = applicationRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("해당 지원서가 존재하지 않습니다."));

		log.info("id {}", id);
		log.info("app {}", app);

		return new ApplicationResponse(app);
	}

	public List<ApplicationResponse> getAppList(UserDetails userDetails) {
		User user = userRepository.findByEmail(userDetails.getUsername())
			.orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

		List<Application> appList = user.getApplications().stream().toList();

		return appList.stream()
			.map(ApplicationResponse::new)
			.toList();
	}

	public String deleteApp(Long id) {
		Application app = applicationRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("해당 지원서가 존재하지 않습니다."));

		applicationRepository.delete(app);

		return "지원서를 삭제하였습니다.";
	}

	public String deleteCustomQuestion(Long id) {
		ApplicationCustom customQuestion = applicationCustomRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("해당 질문이 존재하지 않습니다."));

		applicationCustomRepository.delete(customQuestion);

		return "지원서 추가 항목을 삭제하였습니다.";
	}

	public String updateApp(Long id, ApplicationRequest request) {
		Application app = applicationRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("해당 지원서가 존재하지 않습니다."));

		List<ApplicationCustom> customs = app.getApplicationCustoms().stream().toList();
		for (ApplicationCustom customQ : customs) {
			applicationCustomRepository.delete(customQ);
		}

		Application application = Application.builder()
			.id(id)
			.name(request.getName())
			.motivation(request.getMotivation())
			.teamwork(request.getTeamwork())
			.effort(request.getEffort())
			.aspiration(request.getAspiration())
			.build();

		if (request.getCustomQuestions() != null) {
			for (ApplicationCustomRequest customRequest : request.getCustomQuestions()) {
				ApplicationCustom custom = ApplicationCustom.builder()
					.question(customRequest.getQuestion())
					.answer(customRequest.getAnswer())
					.application(application)
					.build();
				applicationCustomRepository.save(custom);
			}
		}

		applicationRepository.save(application);

		return "지원서가 수정되었습니다.";
	}

	public void saveResume(UserDetails userDetails, ResumeCreateResponse resumeCreateResponse) {
		User user = userRepository.findByEmail(userDetails.getUsername())
			.orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

		resumeRepository.save(Resume.builder()
			.name(resumeCreateResponse.getName())
			.contact(resumeCreateResponse.getContact())
			.academicAbility(resumeCreateResponse.getAcademicAbility())
			.career(resumeCreateResponse.getCareer())
			.user(user).build());
	}

	public ResumeGetResponse getResume(CustomUserDetails userDetails) {
		// User user = getUserByJWT.getCurrentMember();
		User user = userRepository.findByEmail(userDetails.getEmail())
			.orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));
		System.out.println("user12 = " + user.getEmail());

		Resume resume1 = resumeRepository.findByUser(user).orElseThrow(() -> new RuntimeException("이력서가 존재하지 않습니다."));

		return ResumeGetResponse.builder()
			.name(resume1.getName())
			.contact(resume1.getContact())
			.career(resume1.getCareer())
			.userId(resume1.getUser().getId())
			.academicAbility(resume1.getAcademicAbility()).build();

	}

}
