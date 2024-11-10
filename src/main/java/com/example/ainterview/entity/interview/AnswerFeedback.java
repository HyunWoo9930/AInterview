package com.example.ainterview.entity.interview;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class AnswerFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @OneToOne
    private Answer answer;
}
