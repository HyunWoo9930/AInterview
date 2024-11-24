package com.example.ainterview.service;

import com.example.ainterview.domain.user.Application;
import com.example.ainterview.domain.user.ApplicationCustom;
import com.example.ainterview.domain.user.User;
import com.example.ainterview.dto.CustomUserDetails;
import com.example.ainterview.dto.request.ApplicationCustomRequest;
import com.example.ainterview.dto.request.ApplicationRequest;
import com.example.ainterview.dto.response.ApplicationResponse;
import com.example.ainterview.repository.ApplicationCustomRepository;
import com.example.ainterview.repository.ApplicationRepository;
import com.example.ainterview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationCustomRepository applicationCustomRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public String saveApplication(UserDetails userDetails, ApplicationRequest request) {

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

        return "지원서가 성공적으로 저장되었습니다.";
    }

    public ApplicationResponse getApplication(Long id) {
        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 지원서가 존재하지 않습니다."));

        log.info("app {}", app);
        return new ApplicationResponse(app);
    }

}
