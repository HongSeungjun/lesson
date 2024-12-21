package com.join.lesson.core.entity.web;

import com.join.lesson.feature.schedule.dto.RegistScheduleRequest;
import com.join.lesson.feature.schedule.dto.UpdateScheduleRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Slf4j
@Getter
@Table(name = "schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "schedule_name", nullable = false)
    private String scheduleName;

    @Column(name = "schedule_start", nullable = false)
    private LocalDateTime scheduleStart;

    @Column(name = "schedule_end", nullable = false)
    private LocalDateTime scheduleEnd;

    @Column(name = "max_count", nullable = false)
    private int maxCount;

    @Column(name = "reserved_count", nullable = false)
    private int reservedCount;

    @Column(name = "state", nullable = false)
    private int state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private LessonEntity lesson;

    @Builder
    private ScheduleEntity(String scheduleName, LocalDateTime scheduleStart, LocalDateTime scheduleEnd, int maxCount, int reservedCount, int state, LessonEntity lesson) {
        this.scheduleName = scheduleName;
        this.scheduleStart = scheduleStart;
        this.scheduleEnd = scheduleEnd;
        this.maxCount = maxCount;
        this.reservedCount = reservedCount;
        this.state = state;
        this.lesson = lesson;
    }

    public static ScheduleEntity toEntity(RegistScheduleRequest registScheduleRequest, LessonEntity lesson) {
        return ScheduleEntity.builder()
                .scheduleName(registScheduleRequest.getTitle())
                .scheduleStart(registScheduleRequest.getStart())
                .scheduleEnd(registScheduleRequest.getEnd())
                .maxCount(registScheduleRequest.getMaxCount())
                .reservedCount(0)
                .state(0)
                .lesson(lesson)
                .build();
    }

    public static Timestamp stringToTimeStamp(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateFormat.setLenient(false);
        try {
            Date stringToDate = dateFormat.parse(date);
            Timestamp stringToTimestamp = new Timestamp(stringToDate.getTime());

            return stringToTimestamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void changeSchedule(UpdateScheduleRequest updateScheduleRequset) {
        this.scheduleName = updateScheduleRequset.getTitle();
        this.scheduleStart = updateScheduleRequset.getStart();
        this.scheduleEnd = updateScheduleRequset.getEnd();
        this.maxCount = updateScheduleRequset.getMaxCount();
    }

    public void changeSchedule(LocalDateTime start, LocalDateTime end) {
        this.scheduleStart = start;
        this.scheduleEnd = end;
        this.state = 2;

    }

    public void increaseReservedCount() {
        log.info("예약 카운트 증가");
        this.reservedCount += 1;
        if (this.reservedCount == this.maxCount) this.state = 1;
    }

    public void decreaseReservedCount() {
        if (this.reservedCount == this.maxCount) this.state = 0;
        this.reservedCount -= 1;
    }

    public boolean isFullCount() {
        if (this.reservedCount == maxCount) return true;
        return false;
    }

    public void colsed() {
        this.state = 1;
    }

    public void privateReservaionApprove() {
        this.state = 0;
    }
}
