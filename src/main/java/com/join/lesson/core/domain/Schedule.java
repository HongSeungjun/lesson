package com.join.lesson.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Schedule {

    private Long id;
    private String scheduleName;
    private LocalDateTime scheduleStart;
    private LocalDateTime scheduleEnd;
    private int maxCount;
    private int reservedCount;
    private int state;
    private Lesson lesson;

    @Builder
    public Schedule(Long id, String scheduleName, LocalDateTime scheduleStart, LocalDateTime scheduleEnd, int maxCount, int reservedCount, int state, Lesson lesson) {
        this.id = id;
        this.scheduleName = scheduleName;
        this.scheduleStart = scheduleStart;
        this.scheduleEnd = scheduleEnd;
        this.maxCount = maxCount;
        this.reservedCount = reservedCount;
        this.state = state;
        this.lesson = lesson;
    }
}
