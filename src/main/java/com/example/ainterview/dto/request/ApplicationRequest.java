package com.example.ainterview.dto.request;

import com.example.ainterview.domain.user.User;
import lombok.Getter;

import java.util.List;

@Getter
public class ApplicationRequest {

    private String name;
    private String motivation;
    private String teamwork;
    private String effort;
    private String aspiration;
    private List<ApplicationCustomRequest> customQuestions;
}
