package com.example.ainterview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ainterview.domain.user.Resume;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}
