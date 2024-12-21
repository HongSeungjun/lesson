package com.join.lesson.feature.unavailableTimes.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
public class UnavailableTimeResponse {

    private Long unavailableId;

    private int daysOfWeek;

    private String startTime;

    private String endTime;

    @Builder
    public UnavailableTimeResponse(Long unavailableId, int daysOfWeek, LocalTime startTime, LocalTime endTime) {
        this.unavailableId = unavailableId;
        this.daysOfWeek = daysOfWeek;
        this.startTime = startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        this.endTime = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

}
