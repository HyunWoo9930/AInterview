package com.example.ainterview.repository;

import com.example.ainterview.domain.user.ApplicationCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationCustomRepository extends JpaRepository<ApplicationCustom, Long> {
}
