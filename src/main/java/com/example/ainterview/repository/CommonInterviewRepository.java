package com.example.ainterview.repository;

import com.example.ainterview.domain.interview.CommonInterview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonInterviewRepository extends JpaRepository<CommonInterview, Long> {
}
