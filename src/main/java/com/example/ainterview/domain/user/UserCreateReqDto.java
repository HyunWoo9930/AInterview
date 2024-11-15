package com.example.ainterview.domain.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateReqDto {
    private String userId;
    private String password;
    private String nickname;
    private String image;
}
