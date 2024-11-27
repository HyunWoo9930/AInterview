package com.example.ainterview.repository;

import com.example.ainterview.domain.interview.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
