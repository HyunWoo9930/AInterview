package com.example.ainterview.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 3000)
    private String motivation;

    @Column(length = 3000)
    private String teamwork;

    @Column(length = 3000)
    private String effort;

    @Column(length = 3000)
    private String aspiration;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "application", fetch = FetchType.EAGER)
    private Set<ApplicationCustom> applicationCustoms = new HashSet<>();
}
