package com.example.ainterview.dto.response;

import com.example.ainterview.domain.user.User;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResumeGetResponse {
	private Long id;
	private String name;
	private String academicAbility;
	private String career;
	private String contact;
	private Long userId;
}
