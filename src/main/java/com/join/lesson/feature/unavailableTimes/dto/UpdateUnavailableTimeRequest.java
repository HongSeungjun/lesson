package com.join.lesson.feature.unavailableTimes.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class UpdateUnavailableTimeRequest {

    @NotNull
    private Long unavailableId;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @Builder
    public UpdateUnavailableTimeRequest(@NotNull Long unavailableId, @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime) {
        this.unavailableId = unavailableId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
