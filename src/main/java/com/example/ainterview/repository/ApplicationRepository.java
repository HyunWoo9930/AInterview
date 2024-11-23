package com.example.ainterview.repository;

import com.example.ainterview.domain.user.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
