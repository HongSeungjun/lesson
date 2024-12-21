package com.join.lesson.feature.participation.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class RegistParticipantRequest {

    @NotNull
    private Long lessonId;

    @NotEmpty
    @Size(min = 2, max = 16)
    private String loginId;

    @NotNull
    @Min(0)
    private int count;

    @Builder
    private RegistParticipantRequest(Long lessonId, String loginId, int count) {
        this.lessonId = lessonId;
        this.loginId = loginId;
        this.count = count;
    }
}
