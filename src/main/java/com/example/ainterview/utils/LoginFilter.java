package com.example.ainterview.utils;

import com.example.ainterview.dto.CustomUserDetails;
import com.example.ainterview.dto.LoginRequest;
import com.example.ainterview.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;
  private final com.example.ainterview.utils.JwtUtil jwtUtil;

  public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    setFilterProcessesUrl("/api/user/login"); // 커스텀 로그인 엔드포인트 설정
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

      String email = loginRequest.getEmail();
      String password = loginRequest.getPassword();

      log.info("email: {}, password: {}", email, password);

      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
      return authenticationManager.authenticate(authToken);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
    log.info("successfulAuthentication called");
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
    String email = customUserDetails.getUsername();
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    String token = jwtUtil.createToken(email);

    response.addHeader("Authorization", "Bearer " + token);
    response.setContentType("application/json");
    log.info("login success");
  }

  //로그인 실패시 실행하는 메소드
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    response.setStatus(401);
    log.info("login failed");
  }
}