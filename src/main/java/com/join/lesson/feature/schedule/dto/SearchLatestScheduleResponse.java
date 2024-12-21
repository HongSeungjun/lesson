package com.join.lesson.feature.schedule.dto;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class SearchLatestScheduleResponse {

    private String scheduleName;

    private String day;

    private String time;

    private String lessonName;

    private int lessonType;

    @Builder
    public SearchLatestScheduleResponse(String scheduleName, LocalDateTime startTime, String lessonName, int lessonType) {
        this.scheduleName = scheduleName;
        this.day = startTime.toLocalDate().toString();
        this.time = startTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.lessonName = lessonName;
        this.lessonType = lessonType;
    }
}
