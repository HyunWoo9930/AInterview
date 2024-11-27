package com.example.ainterview.domain.interview;

import com.example.ainterview.domain.user.Resume;
import com.example.ainterview.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class IntegratedInterview extends Interview{

    private Boolean isCameraOn;
    private Boolean isManyToOne;

    private String url;

    @OneToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
