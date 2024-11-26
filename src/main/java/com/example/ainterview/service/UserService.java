package com.example.ainterview.service;

import com.example.ainterview.domain.user.User.Gender;
import com.example.ainterview.dto.SignupRequest;
import com.example.ainterview.dto.UserRequest;
import com.example.ainterview.dto.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ainterview.domain.user.User;
import com.example.ainterview.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

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

    public UserResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        return new UserResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        if (userRequest.getName() != null) {
            user.setName(userRequest.getName());
        }
        if (userRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        userRepository.save(user);
        return new UserResponse(user);
    }

    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
}
