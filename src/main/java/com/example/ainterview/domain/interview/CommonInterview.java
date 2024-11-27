package com.example.ainterview.domain.interview;

import com.example.ainterview.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommonInterview extends Interview {

	private Boolean isCameraOn;
	private Boolean isManyToOne;

	private String url;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
