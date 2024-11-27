package com.example.ainterview.repository;

import com.example.ainterview.domain.interview.IntegratedInterview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntegratedInterviewRepository extends JpaRepository<IntegratedInterview, Long> {
}
