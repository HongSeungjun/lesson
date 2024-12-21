package com.join.lesson.feature.reservation.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Getter
public class
ReservationResponse {

    private Long reservationId;

    private Long scheduleId;

    private int state;

    private String title;

    private String start;

    private String end;

    private Long lessonId;

    private String lessonName;

    @Builder
    public ReservationResponse(Long reservationId, Long scheduleId, int state, String title, LocalDateTime start, LocalDateTime end, Long lessonId, String lessonName) {
        this.reservationId = reservationId;
        this.scheduleId = scheduleId;
        this.state = state;
        this.title = title;
        this.start = start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.end = end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.lessonId = lessonId;
        this.lessonName = lessonName;
    }
}
