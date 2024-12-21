package com.join.lesson.feature.reservation.dto;

import com.join.lesson.core.domain.Reservation;
import com.join.lesson.core.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CancelReservationRequest {

    private Long reservationId;

    private String reason;

    @Builder
    public CancelReservationRequest(Long reservationId, String reason) {
        this.reservationId = reservationId;
        this.reason = reason;
    }

    public Reservation toDomain(User member) {
        return Reservation.builder()
                .reservationId(this.reservationId)
                .cancelReaon(this.reason)
                .user(member)
                .build();
    }
}
