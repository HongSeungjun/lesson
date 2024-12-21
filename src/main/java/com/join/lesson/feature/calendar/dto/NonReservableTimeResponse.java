package com.join.lesson.feature.calendar.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NonReservableTimeResponse {

    private String start;
    private String end;
    private int[] dow;

    @Builder
    public NonReservableTimeResponse(String start, String end, int[] dow) {
        this.start = start;
        this.end = end;
        this.dow = dow;
    }

}
