package com.example.ainterview.domain.user;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Getter
public class ResumeCreateResponse {
	private String name;
	private String academicAbility;
	private String career;
	private String contact;
}
