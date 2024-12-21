package com.join.lesson.feature.lesson.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@NoArgsConstructor
@Getter
public class UpdateLessonRequest {

    @NotNull
    private Long lessonId;

    @NotBlank
    @Size(min = 2, max = 16)
    private String lessonName;

    @NotNull
    @Min(0)
    private int lessonType;

    @NotNull
    @Min(0)
    @Max(100)
    private int lessonProperty;

    @Builder
    private UpdateLessonRequest(Long lessonId, String lessonName, int lessonType, int lessonProperty) {
        this.lessonId = lessonId;
        this.lessonName = lessonName;
        this.lessonType = lessonType;
        this.lessonProperty = lessonProperty;
    }
}
