package com.join.lesson.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class UnavailableTimes {

    private Long unavilableId;

    private int dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private User user;

    @Builder
    public UnavailableTimes(Long unavilableId, int dayOfWeek, LocalTime startTime, LocalTime endTime, User user) {
        this.unavilableId = unavilableId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
    }

}
