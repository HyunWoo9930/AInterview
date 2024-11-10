package com.example.ainterview.entity.interview;


import com.example.ainterview.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int qNumber;
    private Long costTime; //ms로 저장

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
