package com.join.lesson.core.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class Lesson {
    private Long id;
    private String lessonName;
    private int lessonType;
    private int lessonProperty;
    private User user;
    private Timestamp createdDate;
    private Timestamp modifiedDate;

    @Builder
    public Lesson(Long id, String lessonName, int lessonType, int lessonProperty, User user, Timestamp createdDate, Timestamp modifiedDate) {
        this.id = id;
        this.lessonName = lessonName;
        this.lessonType = lessonType;
        this.lessonProperty = lessonProperty;
        this.user = user;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

}


