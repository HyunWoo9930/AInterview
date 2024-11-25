package com.example.ainterview.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {
  private String email;
  private String name;
  private String image;
  private String password;
}
