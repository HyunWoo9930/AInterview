package com.example.ainterview.dto.request;

import com.example.ainterview.domain.user.User;
import lombok.Getter;

@Getter
public class ApplicationRequest {

    private String name;
    private String reason;
    private String groupProject;
    private String effort;
    private String plan;
}
