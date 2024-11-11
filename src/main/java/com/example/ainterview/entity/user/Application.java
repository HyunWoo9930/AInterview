package com.example.ainterview.entity.user;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 3000)
    private String reason;

    @Column(length = 3000, name = "group_name")
    private String groupName;

    @Column(length = 3000)
    private String effort;

    @Column(length = 3000)
    private String plan;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
