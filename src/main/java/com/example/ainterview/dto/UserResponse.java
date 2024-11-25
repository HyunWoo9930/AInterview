package com.example.ainterview.dto;

import com.example.ainterview.domain.user.User;
import com.example.ainterview.domain.user.User.Gender;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UserResponse {
  private final Long id;
  private final String name;
  private final String email;
  private final Gender gender;
  private final LocalDateTime createdAt;

  public UserResponse(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.email = user.getEmail();
    this.gender = user.getGender();
    this.createdAt = user.getCreatedAt();
  }
}
