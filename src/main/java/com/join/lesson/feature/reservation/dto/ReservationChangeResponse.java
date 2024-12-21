package com.join.lesson.feature.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationChangeResponse {

    private Long changeId;

    private LocalDateTime originStart;

    private LocalDateTime originEnd;

    private LocalDateTime requestStart;

    private LocalDateTime requestEnd;

    private int state;

    private String reason;

    private Long reservationId;

    private Long unavailableId;


}
