package com.example.ainterview.domain.interview;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @OneToOne
    private Interview interview;
}
