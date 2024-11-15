package com.example.ainterview.service;

import org.springframework.stereotype.Service;

import com.example.ainterview.domain.user.User;
import com.example.ainterview.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
}
