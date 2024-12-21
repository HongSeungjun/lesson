package com.join.lesson.feature.calendar;

import com.join.lesson.feature.calendar.dto.NonReservableTimeResponse;
import com.join.lesson.feature.schedule.dto.ScheduleResponse;
import com.join.lesson.feature.unavailableTimes.dto.UnavailableTimeResponse;
import com.join.lesson.share.calendar.CalendarService;
import com.join.lesson.share.schedule.ScheduleService;
import com.join.lesson.share.unavailabletime.UnavailableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CalendarUsecase implements CalendarService {

    private final ScheduleService scheduleService;

    private final UnavailableService unavailableService;

    // 해당하는 pro가 안되는 시간 가져오기
    public List<NonReservableTimeResponse> getCalendarEvents(Long proId) {
        List<NonReservableTimeResponse> events = new ArrayList<>();

        List<UnavailableTimeResponse> unavailableTimes = unavailableService.getUnavailableTimes(proId);
        for (UnavailableTimeResponse ut : unavailableTimes) {
            events.add(NonReservableTimeResponse.builder()
                    .start(ut.getStartTime())
                    .end(ut.getEndTime())
                    .dow(new int[]{ut.getDaysOfWeek()})
                    .build()
            );
        }

        List<ScheduleResponse> schedules = scheduleService.getSchedulesByProId(proId);
        for (ScheduleResponse schedule : schedules) {
            events.add(NonReservableTimeResponse.builder()
                    .start(schedule.getStart())
                    .end(schedule.getEnd())
                    .build());
        }

        return events;
    }

    @Override
    public Boolean isExistTime(LocalDateTime requestStart, LocalDateTime requestEnd, Long proId) {
        List<NonReservableTimeResponse> nonReservableTimeList = getCalendarEvents(proId);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (NonReservableTimeResponse nonReservableTime : nonReservableTimeList) {
            LocalDateTime startTime;
            LocalDateTime endTime;
            log.info("non :{} ", nonReservableTime.getStart());
            if (nonReservableTime.getStart().contains("T")) {
                startTime = LocalDateTime.parse(nonReservableTime.getStart());
                endTime = LocalDateTime.parse(nonReservableTime.getEnd());
            } else {
                LocalTime startLocalTime = LocalTime.parse(nonReservableTime.getStart(), timeFormatter);
                LocalTime endLocalTime = LocalTime.parse(nonReservableTime.getEnd(), timeFormatter);
                int dow = nonReservableTime.getDow()[0];
                if(dow == 0) dow = 7;
                log.info("dow :{} ", dow);
                startTime = requestStart.with(DayOfWeek.of(dow)).with(startLocalTime);
                endTime = requestStart.with(DayOfWeek.of(dow)).with(endLocalTime);
            }

            if ((requestStart.isBefore(endTime) && requestStart.isAfter(startTime)) ||
                    (requestEnd.isAfter(startTime) && requestEnd.isBefore(endTime)) ||
                    (requestStart.isEqual(startTime) || requestEnd.isEqual(endTime))) {
                log.info("reendTime :{} restartTime :{}", requestEnd, requestStart);

                log.info("endTime :{} startTime :{}", endTime, startTime);
                return true;
            }
        }

        return false;
    }


    private LocalDate getNextDayOfWeek(LocalDate currentDate, DayOfWeek dayOfWeek) {
        int daysToAdd = dayOfWeek.getValue() - currentDate.getDayOfWeek().getValue();
        if (daysToAdd < 0) {
            daysToAdd += 7;
        }
        return currentDate.plusDays(daysToAdd);
    }

}
