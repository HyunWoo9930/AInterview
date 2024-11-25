package com.example.ainterview.dto;

import java.time.LocalDateTime;

import com.example.ainterview.domain.user.User;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionsResponse {
	private Long id;
	private Long userId;
	private String question;
	private LocalDateTime createdAt;
}
