package com.example.ainterview.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.ainterview.domain.oauth2.CustomOAuth2User;
import com.example.ainterview.domain.oauth2.KakaoResponse;
import com.example.ainterview.domain.oauth2.OAuth2Response;
import com.example.ainterview.domain.oauth2.OAuth2UserDTO;
import com.example.ainterview.domain.user.User;
import com.example.ainterview.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;

	public OAuth2UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

}
