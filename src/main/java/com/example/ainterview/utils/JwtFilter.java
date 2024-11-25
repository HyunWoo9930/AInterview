package com.example.ainterview.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import com.example.ainterview.domain.user.User;
import com.example.ainterview.dto.CustomUserDetails;
import com.example.ainterview.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.ainterview.domain.oauth2.CustomOAuth2User;
import com.example.ainterview.domain.oauth2.OAuth2UserDTO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public JwtFilter(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("wrong token");
            filterChain.doFilter(request, response);
            return;
        }
        log.info("authorization now");

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];
        //토큰 소멸 시간 검증
        if (jwtUtil.isTokenExpired(token)) {
            log.info("token expired");
            filterChain.doFilter(request, response);
            return;
        }

        //토큰에서 email 획득
        String email = jwtUtil.getUserId(token);
        System.out.println("email = " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        System.out.println("user = " + user.getName());
        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        //스프링 시큐리티 인증 토큰 생성
        Authentication accessToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(accessToken);

        filterChain.doFilter(request, response);
    }
}