package com.join.lesson.feature.unavailableTimes.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class RegistUnavailalbeTimeRequest {

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @Builder
    public RegistUnavailalbeTimeRequest(@NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
