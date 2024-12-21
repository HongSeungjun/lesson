package com.join.lesson.feature.lesson.dto;

import lombok.*;

@Getter
public class LessonResponse {

    private Long lessonId;

    private String lessonName;

    private int lessonType;

    private int lessonProperty;

     @Builder
    public LessonResponse(Long lessonId, String lessonName, int lessonType, int lessonProperty) {
        this.lessonId = lessonId;
        this.lessonName = lessonName;
        this.lessonType = lessonType;
        this.lessonProperty = lessonProperty;
    }

}
