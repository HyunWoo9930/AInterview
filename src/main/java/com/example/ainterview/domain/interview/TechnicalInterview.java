package com.example.ainterview.domain.interview;

import com.example.ainterview.domain.user.Resume;
import com.example.ainterview.domain.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class TechnicalInterview extends Interview {

	private boolean isCameraOn;

	@OneToOne
	@JoinColumn(name = "resume_id")
	private Resume resume;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
