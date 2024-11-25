package com.example.ainterview.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ainterview.domain.user.Resume;
import com.example.ainterview.domain.user.User;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
	Optional<Resume> findByUser(User user);
}
