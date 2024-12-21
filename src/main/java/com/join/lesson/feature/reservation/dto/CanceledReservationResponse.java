package com.join.lesson.feature.reservation.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CanceledReservationResponse {

    private Long reservationId;

    private String cancelReason;

    private String memberName;

    private String memberLoginId;

    private String scheduleName;

    private String start;

    private String end;


    @Builder
    public CanceledReservationResponse(Long reservationId, String cancelReason, String memberName, String memberLoginId, String scheduleName, LocalDateTime start, LocalDateTime end) {
        this.reservationId = reservationId;
        this.cancelReason = cancelReason;
        this.memberName = memberName;
        this.memberLoginId = memberLoginId;
        this.scheduleName = scheduleName;
        this.start = start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.end = end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
