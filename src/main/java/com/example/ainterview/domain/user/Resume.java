package com.example.ainterview.domain.user;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String academicAbility;
    private String content;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
