package com.example.ainterview.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ainterview.domain.user.User;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByProviderId(String id);
}
