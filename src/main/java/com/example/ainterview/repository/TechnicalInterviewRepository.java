package com.example.ainterview.repository;

import com.example.ainterview.domain.interview.TechnicalInterview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnicalInterviewRepository extends JpaRepository<TechnicalInterview, Long> {
}
