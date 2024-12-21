package com.join.lesson.feature.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleWithReservationStatusResponse {

    private Long scheduleId;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private int maxCount;
    private int reservedCount;
    private int state;
    // 사용자 예약 여부
    private boolean isReserved;
    private String lessonName;
    private Long lessonId;
    private Long reservationId;

}
