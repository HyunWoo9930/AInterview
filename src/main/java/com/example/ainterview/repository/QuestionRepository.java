package com.example.ainterview.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ainterview.domain.user.Questions;
import com.example.ainterview.domain.user.User;

public interface QuestionRepository extends JpaRepository<Questions, Long> {
	List<Questions> findAllByUserAndCreatedAtBetween(User user, LocalDateTime startOfDay, LocalDateTime endOfDay);

	List<Questions> findAllByUserAndCreatedAtBefore(User user, LocalDateTime dateTime);

}
