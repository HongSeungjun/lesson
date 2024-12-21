package com.join.lesson.feature.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistPrivateReservationRequest {

    @NotNull
    private Long lessonId;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

}
