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

}
