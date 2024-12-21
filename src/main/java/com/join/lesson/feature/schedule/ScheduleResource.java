package com.join.lesson.feature.schedule;

import com.join.lesson.core.common.ApiResponse;
import com.join.lesson.core.common.util.SecurityUtils;
import com.join.lesson.feature.schedule.dto.RegistScheduleRequest;
import com.join.lesson.feature.schedule.dto.ScheduleResponse;
import com.join.lesson.feature.schedule.dto.ScheduleWithReservationStatusResponse;
import com.join.lesson.feature.schedule.dto.SearchLatestScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleResource {

    private final ScheduleUseCase scheduleUseCase;

    @GetMapping("/pro")
    public ApiResponse<List<ScheduleResponse>> getSchedulesForPro() {


        return ApiResponse.ok(scheduleUseCase.getScheduleForPro(SecurityUtils.getUserId()));

    }

    @GetMapping("/{lessonId}")
    public ApiResponse<List<ScheduleResponse>> getSchedules(@PathVariable Long lessonId) {

        return ApiResponse.ok(scheduleUseCase.getSchedule(lessonId));

    }
    @GetMapping("/{lessonId}/with-reservation-status")
    public ApiResponse<List<ScheduleWithReservationStatusResponse>> getScheduleWithReservationStatus(@PathVariable Long lessonId) {

        return ApiResponse.ok(scheduleUseCase.getScheduleWithReservationStatus(lessonId,SecurityUtils.getUserId()));

    }



    @GetMapping("/pro/cond")
    public ApiResponse<List<SearchLatestScheduleResponse>> getSchedulesOrder() {


        return ApiResponse.ok(scheduleUseCase.searchLatestSchedule(SecurityUtils.getUserId()));

    }

    @PostMapping
    public ApiResponse<Long> registSchedule(@Validated @RequestBody RegistScheduleRequest registScheduleRequest) {

        return ApiResponse.ok(scheduleUseCase.registSchedule(registScheduleRequest));

    }


}
