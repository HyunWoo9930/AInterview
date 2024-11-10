package com.example.ainterview.entity.interview;

import com.example.ainterview.entity.user.Resume;
import com.example.ainterview.entity.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;

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
}
