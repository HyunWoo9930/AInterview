package com.example.ainterview.service;

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

    public User createUser(String userId, String nickname, String image) {
        User newUser = new User();
        newUser.setProviderId(userId);
        newUser.setNickname(nickname);
        newUser.setImage(image);
        newUser.setRole("ROLE_USER");
        return userRepository.save(newUser);

    }

    public User findUserByUserId(String userId) {
        return userRepository.findByProviderId(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
    }

    public void joinProcess(SignupRequest signupRequest) {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new RuntimeException("중복된 이메일입니다.");
        }
        User newUser = new User();
        newUser.setEmail(signupRequest.getEmail());
        newUser.setName(signupRequest.getName());
        newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        newUser.setGender(signupRequest.getGender());
        userRepository.save(newUser);
    }
}
