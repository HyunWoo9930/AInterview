package com.example.ainterview.repository;

import com.example.ainterview.domain.interview.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
}
