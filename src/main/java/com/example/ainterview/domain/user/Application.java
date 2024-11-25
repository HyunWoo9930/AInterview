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

    @Lob
    private String motivation;

    @Lob
    private String teamwork;

    @Lob
    private String effort;

    @Lob
    private String aspiration;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ApplicationCustom> applicationCustoms;

    @Override
    public String toString() {
        return "Application{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", motivation='" + motivation + '\'' +
            ", teamwork='" + teamwork + '\'' +
            ", effort='" + effort + '\'' +
            ", aspiration='" + aspiration + '\'' +
            ", user=" + user +
            ", applicationCustoms=" + applicationCustoms +
            '}';
    }
}
