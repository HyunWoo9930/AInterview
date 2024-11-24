package com.example.ainterview.dto;

import com.example.ainterview.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
  private final User user;

  public CustomUserDetails(User user) {
    this.user = user;
  }

  public Long getId() {
    return user.getId();
  }

  public String getNickname() {
    return user.getNickname();
  }

  public String getImage() {
    return user.getImage();
  }

  public String getGender() {
    return user.getGender().name();
  }

  public String getEmail() {
    return user.getEmail();
  }

  public String getName() {
    return user.getName();
  }

  public String getRole() {
    return user.getRole();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(() -> user.getRole()); // Lambda for GrantedAuthority
    return authorities;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail(); // 로그인 식별자로 이메일 사용
  }


  @Override
  public boolean isAccountNonExpired() {
    return true; // 계정 만료 여부 관리하지 않음
  }

  @Override
  public boolean isAccountNonLocked() {
    return true; // 계정 잠금 여부 관리하지 않음
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true; // 자격 증명 만료 여부 관리하지 않음
  }

  @Override
  public boolean isEnabled() {
    return true; // 계정 활성화 여부 관리하지 않음
  }
}