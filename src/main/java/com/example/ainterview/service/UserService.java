package com.example.ainterview.service;

import com.example.ainterview.domain.user.User.Gender;
import com.example.ainterview.dto.SignupRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ainterview.domain.user.User;
import com.example.ainterview.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void joinProcess(SignupRequest signupRequest) {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new RuntimeException("중복된 이메일입니다.");
        }
        User newUser = new User();
        Gender gender = Gender.valueOf(signupRequest.getGender().toUpperCase());
        newUser.setEmail(signupRequest.getEmail());
        newUser.setName(signupRequest.getName());
        newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        newUser.setGender(gender);
        userRepository.save(newUser);
    }
}
