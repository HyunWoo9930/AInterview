package com.example.ainterview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ainterview.domain.interview.AnswerFeedback;

public interface AnswerFeedbackRepository extends JpaRepository<AnswerFeedback, Long> {
}
