package com.example.ainterview.service;

import org.springframework.stereotype.Service;

import com.example.ainterview.domain.user.User;
import com.example.ainterview.domain.user.UserCreateReqDto;
import com.example.ainterview.utils.JwtUtil;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserManager {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public Cookie createUser(UserCreateReqDto dto) {
        User newUser = userService.createUser(dto.getUserId(), dto.getNickname(), dto.getImage());
        String token = jwtUtil.createToken(newUser.getProviderId(), newUser.getRole());
        return jwtUtil.createCookie("Authorization", token);
    }

    public Cookie login(String userId) {
        User selectedUser = userService.findUserByUserId(userId);
        String token = jwtUtil.createToken(selectedUser.getProviderId(), selectedUser.getRole());
        return jwtUtil.createCookie("Authorization", token);
    }

}
