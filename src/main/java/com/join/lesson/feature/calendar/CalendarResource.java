package com.join.lesson.feature.calendar;

import com.join.lesson.core.common.ApiResponse;
import com.join.lesson.feature.calendar.dto.NonReservableTimeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendars")
public class CalendarResource {

    private final CalendarUsecase calendarUsecase;

    @GetMapping("/{proId}")
    public ApiResponse<List<NonReservableTimeResponse>> getUnavailableTimes(@PathVariable Long proId) {
        return ApiResponse.ok(calendarUsecase.getCalendarEvents(proId));
    }


}
