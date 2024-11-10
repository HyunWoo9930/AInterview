package com.example.ainterview.entity.user;


import com.example.ainterview.entity.interview.CommonInterview;
import com.example.ainterview.entity.interview.IntegratedInterview;
import com.example.ainterview.entity.interview.TechnicalInterview;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<TechnicalInterview> tInterviews;

    @OneToMany(mappedBy = "user")
    private Set<CommonInterview> cInterviews;

    @OneToMany(mappedBy = "user")
    private Set<IntegratedInterview> iInterviews;

    @OneToMany(mappedBy = "user")
    private Set<Application> applications;
}
