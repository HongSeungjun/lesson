package com.join.lesson.share.schedule;

import com.join.lesson.core.entity.web.ScheduleEntity;
import com.join.lesson.feature.schedule.dto.ScheduleResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService {

    ScheduleEntity getScheduleById(Long scheduleId);

    void increaseReservedCount(Long scheduleId);

    List<ScheduleResponse> getSchedulesByProId(Long proId);

    ScheduleEntity registPrivateSchedule(Long lessonId, LocalDateTime start,  LocalDateTime end);

}
