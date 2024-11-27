package com.example.ainterview.domain.interview;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Answer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String content;

	@OneToOne
	@JoinColumn(name = "question_id", nullable = false)
	private Question question;
}
