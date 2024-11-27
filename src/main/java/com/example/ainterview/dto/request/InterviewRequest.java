package com.example.ainterview.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InterviewRequest {

    private String url;
    private String type;
    private Boolean isCameraOn;
    private Boolean isManyToOne;
}
