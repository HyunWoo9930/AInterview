package com.example.ainterview.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.ainterview.repository.UserRepository;
import com.example.ainterview.utils.CustomSuccessHandler;
import com.example.ainterview.utils.JwtFilter;
import com.example.ainterview.utils.JwtUtil;
import com.example.ainterview.utils.LoginFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomSuccessHandler customSuccessHandler;
	private final AuthenticationConfiguration authenticationConfiguration;
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	// CORS 설정
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:5173"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS"));
		configuration.setAllowedHeaders(
			Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			.addFilterBefore(new JwtFilter(userRepository, jwtUtil), UsernamePasswordAuthenticationFilter.class)

			               .oauth2Login(Customizer.withDefaults())
			// .oauth2Login(oauth2 -> oauth2
			// 	.loginPage("/login")
			// 	.successHandler(customSuccessHandler))
			//
			.authorizeHttpRequests(request -> request
				.requestMatchers("/api/**", "/**", "/oauth2/**", "/login/**", "/swagger-ui/**", "/v3/api-docs/**",
					"/swagger-resources/**").permitAll()
				.anyRequest().authenticated()
			)

			.addFilterBefore(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
				UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}


