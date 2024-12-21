package com.join.lesson.feature.schedule.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class RegistScheduleRequest {

    @NotNull
    private Long lessonId;

    @NotBlank
    @Size(min = 2, max = 16)
    private String title;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    @NotNull
    @Min(0)
    @Max(100)
    private int maxCount;

    @Builder
    public RegistScheduleRequest(Long lessonId, String title, LocalDateTime start, LocalDateTime end, int maxCount) {
        this.lessonId = lessonId;
        this.title = title;
        this.start = start;
        this.end = end;
        this.maxCount = maxCount;
    }
}
