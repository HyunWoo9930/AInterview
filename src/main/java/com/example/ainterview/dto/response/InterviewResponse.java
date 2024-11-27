package com.example.ainterview.dto.response;

import com.example.ainterview.domain.interview.Interview;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InterviewResponse {

    private Long interviewId;

    public InterviewResponse(Interview interview) {
        this.interviewId = interview.getId();
    }
}
