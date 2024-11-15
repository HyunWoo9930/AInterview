package com.example.ainterview.domain.interview;

import com.example.ainterview.domain.user.Resume;
import com.example.ainterview.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class IntegratedInterview {

    private boolean isCameraOn;
    private boolean isManyTooOne;

    private String url;

    @OneToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Getter
    @Setter
    @Id
    private Long id;

}
