package com.join.lesson.feature.schedule;

import com.join.lesson.core.entity.web.LessonEntity;
import com.join.lesson.core.entity.web.ScheduleEntity;
import com.join.lesson.core.common.exception.BaseException;
import com.join.lesson.core.common.exception.ErrorCode;
import com.join.lesson.feature.schedule.dto.*;
import com.join.lesson.feature.schedule.dto.*;
import com.join.lesson.feature.schedule.repository.web.ScheduleQueryRepository;
import com.join.lesson.feature.schedule.repository.web.ScheduleRepositroy;
import com.join.lesson.share.lesson.LessonService;
import com.join.lesson.share.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleUseCase implements ScheduleService {

    private final ScheduleRepositroy scheduleRepositroy;
    private final ScheduleQueryRepository scheduleQueryRepository;
    private final LessonService lessonService;

    public Long registSchedule(RegistScheduleRequest registScheduleRequest) {

        LessonEntity lesson = lessonService.getLessonById(registScheduleRequest.getLessonId());

        ScheduleEntity schedule = ScheduleEntity.toEntity(registScheduleRequest, lesson);

        scheduleRepositroy.save(schedule);

        return schedule.getId();
    }

    @Transactional
    public Long updateSchedule(UpdateScheduleRequest updateScheduleRequest) {

        ScheduleEntity schedule = scheduleRepositroy.findById(updateScheduleRequest.getScheduleId()).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_SCHEDULE));

        schedule.changeSchedule(updateScheduleRequest);

        return schedule.getId();
    }


    public List<ScheduleResponse> getScheduleForPro(Long proId) {

        return scheduleQueryRepository.getScheduleForPro(proId);

    }

    public List<ScheduleResponse> getSchedule(Long lessonId) {

        return scheduleQueryRepository.getSchedule(lessonId);

    }

    public List<SearchLatestScheduleResponse> searchLatestSchedule(Long userId) {

        return scheduleQueryRepository.getScheduleFastetAsToday(userId);

    }

    public Long deleteSchedule(Long scheduleId) {

        return 1L;

    }

    @Override
    public ScheduleEntity getScheduleById(Long scheduleId) {

        return scheduleRepositroy.findById(scheduleId).get();

    }

    @Transactional
    @Override
    public void increaseReservedCount(Long scheduleId) {

        scheduleQueryRepository.increaseReservedCount(scheduleId);

    }

    // 프로가 가진 모든 스케줄 조회
    @Override
    public List<ScheduleResponse> getSchedulesByProId(Long proId) {

        return scheduleQueryRepository.getScheduleByProId(proId);

    }

    @Override
    public ScheduleEntity registPrivateSchedule(Long lessonId, LocalDateTime start, LocalDateTime end) {
        return scheduleRepositroy.save(ScheduleEntity.builder()
                .lesson(lessonService.getLessonById(lessonId))
                .scheduleStart(start)
                .scheduleEnd(end)
                .maxCount(1)
                .reservedCount(1)
                .scheduleName("개인레슨")
                .state(2)
                .build());

    }

    public List<ScheduleWithReservationStatusResponse> getScheduleWithReservationStatus(Long lessonId, Long userId) {

        return scheduleQueryRepository.getScheduleWithReservationStatus(lessonId,userId);

    }
}
