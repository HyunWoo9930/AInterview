package com.example.ainterview.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
  private String email;
  private String name;
  private String password;
  private String gender;
}
