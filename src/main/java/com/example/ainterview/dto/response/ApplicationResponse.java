package com.example.ainterview.dto.response;

import com.example.ainterview.domain.user.Application;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
@NoArgsConstructor
public class ApplicationResponse {

    private Long applicationId;
    private String name;
    private String motivation;
    private String teamwork;
    private String effort;
    private String aspiration;

    private List<CustomQuestionResponse> customQuestions;

    public ApplicationResponse(Application application) {
        log.info("response 실행");
        this.applicationId = application.getId();
        this.name = application.getName();
        this.motivation = application.getMotivation();
        this.teamwork = application.getTeamwork();
        this.effort = application.getEffort();
        this.aspiration = application.getAspiration();
        this.customQuestions = application.getApplicationCustoms().stream().map(CustomQuestionResponse::new).toList();
    }
}
