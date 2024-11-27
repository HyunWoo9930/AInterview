package com.example.ainterview.domain.user;


import com.example.ainterview.domain.interview.CommonInterview;
import com.example.ainterview.domain.interview.IntegratedInterview;
import com.example.ainterview.domain.interview.TechnicalInterview;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String image;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String email;
    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "user")
    private Set<TechnicalInterview> tInterviews;

    @OneToMany(mappedBy = "user")
    private Set<CommonInterview> cInterviews;

    @OneToMany(mappedBy = "user")
    private Set<IntegratedInterview> iInterviews;

    @OneToMany(mappedBy = "user")
    private Set<Application> applications;

    @OneToOne(mappedBy = "user")
    private Resume resume;

    public enum Gender {
        F, M,
    }
}
