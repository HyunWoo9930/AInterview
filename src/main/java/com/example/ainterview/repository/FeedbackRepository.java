package com.example.ainterview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ainterview.domain.interview.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}