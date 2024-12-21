package com.join.lesson.feature.schedule.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ScheduleResponse {

    private Long scheduleId;

    private String title;

    private String start;

    private String end;

    private int maxCount;

    private int reservedCount;

    private int state;

    private String lessonName;

    private Long lessonId;

    @Builder
    public ScheduleResponse(Long scheduleId, String title, LocalDateTime start, LocalDateTime end, int maxCount, int reservedCount, int state, String lessonName, Long lessonId) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.start = start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.end = end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.maxCount = maxCount;
        this.reservedCount = reservedCount;
        this.state = state;
        this.lessonName = lessonName;
        this.lessonId = lessonId;
    }
}
