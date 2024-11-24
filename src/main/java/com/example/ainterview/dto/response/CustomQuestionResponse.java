package com.example.ainterview.dto.response;

import com.example.ainterview.domain.user.ApplicationCustom;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomQuestionResponse {
    private Long questionId;
    private String question;
    private String answer;

    public CustomQuestionResponse(ApplicationCustom custom) {
        this.questionId = custom.getId();
        this.question = custom.getQuestion();
        this.answer = custom.getAnswer();
    }
}
