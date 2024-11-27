package com.example.ainterview.repository;

import com.example.ainterview.domain.interview.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewQuestionRepository extends JpaRepository<Question, Long> {
}
