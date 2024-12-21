package com.join.lesson.feature.schedule.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class UpdateScheduleRequest {

    @NotNull
    private Long scheduleId;

    @NotEmpty
    @Size(min = 2, max = 16)
    private String title;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    @NotNull
    private int maxCount;

    @Builder
    private UpdateScheduleRequest(Long scheduleId, String title, LocalDateTime start, LocalDateTime end, int maxCount) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.start = start;
        this.end = end;
        this.maxCount = maxCount;
    }
}
