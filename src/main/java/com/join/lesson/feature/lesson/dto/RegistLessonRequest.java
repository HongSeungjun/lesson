package com.join.lesson.feature.lesson.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class RegistLessonRequest {

    @NotBlank
    @Size(min = 1, max = 16)
    private String lessonName;

    @NotNull
    @Min(0)
    private int lessonType;

    @NotNull
    @Min(0)
    private int lessonProperty;

    @Builder
    private RegistLessonRequest(String lessonName, int lessonType, int lessonProperty) {
        this.lessonName = lessonName;
        this.lessonType = lessonType;
        this.lessonProperty = lessonProperty;
    }

}
