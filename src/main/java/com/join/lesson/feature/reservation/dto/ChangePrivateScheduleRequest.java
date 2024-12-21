package com.join.lesson.feature.reservation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChangePrivateScheduleRequest {

    @NotNull
    private Long reservationId;

    @NotNull
    private Long proId;

    @NotNull
    private LocalDateTime originStart;

    @NotNull
    private LocalDateTime originEnd;

    @NotNull
    private LocalDateTime requestedStart;

    @NotNull
    private LocalDateTime requestedEnd;

    @NotBlank
    private String reason;

    @Builder
    public ChangePrivateScheduleRequest(@NotNull Long reservationId, @NotNull Long proId, @NotNull LocalDateTime originStart, @NotNull LocalDateTime originEnd, @NotNull LocalDateTime requestedStart, @NotNull LocalDateTime requestedEnd, @NotBlank String reason) {
        this.reservationId = reservationId;
        this.proId = proId;
        this.originStart = originStart;
        this.originEnd = originEnd;
        this.requestedStart = requestedStart;
        this.requestedEnd = requestedEnd;
        this.reason = reason;
    }
}
