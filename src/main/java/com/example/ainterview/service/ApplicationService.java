package com.example.ainterview.service;

import com.example.ainterview.domain.user.User;
import com.example.ainterview.dto.request.ApplicationRequest;
import com.example.ainterview.repository.ApplicationRepository;
import com.example.ainterview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public String saveApplication(UserDetails userDetails, ApplicationRequest request) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));


    }

}
