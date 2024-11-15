package com.example.ainterview.domain.interview;

import com.example.ainterview.domain.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class CommonInterview extends Interview {

	private boolean isCameraOn;
	private boolean isManyTooOne;

	private String url;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
