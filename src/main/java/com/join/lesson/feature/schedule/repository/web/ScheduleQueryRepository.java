package com.join.lesson.feature.schedule.repository.web;

import com.join.lesson.core.entity.web.QReservationEntity;
import com.join.lesson.core.entity.web.QScheduleEntity;
import com.join.lesson.feature.schedule.dto.ScheduleResponse;
import com.join.lesson.feature.schedule.dto.ScheduleWithReservationStatusResponse;
import com.join.lesson.feature.schedule.dto.SearchLatestScheduleResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.join.lesson.core.entity.web.QLessonEntity.*;
import static com.join.lesson.core.entity.web.QScheduleEntity.*;
import static com.join.lesson.core.entity.web.QUserEntity.*;

@Repository
public class ScheduleQueryRepository {
    private final JPAQueryFactory queryFactory;


    public ScheduleQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ScheduleResponse> getScheduleForPro(Long proId) {
        return queryFactory
                .select(Projections.constructor(ScheduleResponse.class,
                        scheduleEntity.id,
                        scheduleEntity.scheduleName.as("title"),
                        scheduleEntity.scheduleStart.as("start"),
                        scheduleEntity.scheduleEnd.as("end"),
                        scheduleEntity.maxCount,
                        scheduleEntity.reservedCount,
                        scheduleEntity.state,
                        scheduleEntity.lesson.lessonName,
                        scheduleEntity.lesson.id
                ))
                .from(scheduleEntity)
                .join(scheduleEntity.lesson, lessonEntity)
                .where(scheduleEntity.lesson.userEntity.id.eq(proId),
                        scheduleEntity.state.eq(2).not())
                .fetch();
    }


    public List<ScheduleResponse> getSchedule(Long lessonId) {
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime sevenDaysLater = now.plusDays(7).plusDays(1);
        return queryFactory
                .select(Projections.constructor(ScheduleResponse.class,
                        scheduleEntity.id,
                        scheduleEntity.scheduleName,
                        scheduleEntity.scheduleStart,
                        scheduleEntity.scheduleEnd,
                        scheduleEntity.maxCount,
                        scheduleEntity.reservedCount,
                        scheduleEntity.state,
                        scheduleEntity.lesson.lessonName,
                        scheduleEntity.lesson.id
                ))
                .from(scheduleEntity)
                .join(scheduleEntity.lesson, lessonEntity)
                .where(scheduleEntity.lesson.id.eq(lessonId),
                        scheduleEntity.scheduleStart.between(now, sevenDaysLater))
                .fetch();
    }

    public long increaseReservedCount(Long scheduleId) {
        return queryFactory
                .update(scheduleEntity)
                .set(scheduleEntity.reservedCount, scheduleEntity.reservedCount.add(1))
                .where(scheduleEntity.id.eq(scheduleId))
                .execute();
    }

    public List<SearchLatestScheduleResponse> getScheduleFastetAsToday(Long proId) {
        LocalDate today = LocalDate.now();
        LocalDateTime afterSevenDays = today.plusDays(7).atStartOfDay();

        return queryFactory
                .select(Projections.constructor(SearchLatestScheduleResponse.class,
                        scheduleEntity.scheduleName,
                        scheduleEntity.scheduleStart,
                        scheduleEntity.lesson.lessonName,
                        scheduleEntity.lesson.lessonType
                ))
                .from(scheduleEntity)
                .innerJoin(scheduleEntity.lesson, lessonEntity)
                .innerJoin(lessonEntity.userEntity, userEntity)
                .where(userEntity.id.eq(proId)
                        .and(scheduleEntity.scheduleStart.between(LocalDateTime.now(), afterSevenDays)))
                .orderBy(scheduleEntity.scheduleStart.asc())
                .fetch();
    }

    private BooleanExpression eqLessonId(Long lessonId) {
        return lessonId != null ? lessonEntity.id.eq(lessonId) : null;
    }

    public List<ScheduleResponse> getScheduleByProId(Long proId) {
        return queryFactory
                .select(Projections.constructor(ScheduleResponse.class,
                        scheduleEntity.id,
                        scheduleEntity.scheduleName,
                        scheduleEntity.scheduleStart,
                        scheduleEntity.scheduleEnd,
                        scheduleEntity.maxCount,
                        scheduleEntity.reservedCount,
                        scheduleEntity.state,
                        scheduleEntity.lesson.lessonName,
                        scheduleEntity.lesson.id
                ))
                .from(scheduleEntity)
                .join(scheduleEntity.lesson, lessonEntity)
                .where(scheduleEntity.lesson.userEntity.id.eq(proId))
                .fetch();
    }

    public List<ScheduleWithReservationStatusResponse> getScheduleWithReservationStatus(Long lessonId, Long userId) {
        QScheduleEntity schedule = scheduleEntity;
        QReservationEntity reservation = QReservationEntity.reservationEntity;
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime sevenDaysLater = now.plusDays(7).plusDays(1);
        return queryFactory
                .select(Projections.constructor(
                        ScheduleWithReservationStatusResponse.class,
                        schedule.id,
                        schedule.scheduleName,
                        schedule.scheduleStart,
                        schedule.scheduleEnd,
                        schedule.maxCount,
                        schedule.reservedCount,
                        schedule.state,
                        reservation.id.isNotNull().as("isReserved"),
                        scheduleEntity.lesson.lessonName,
                        scheduleEntity.lesson.id,
                        reservation.id
                ))
                .from(schedule)
                .join(scheduleEntity.lesson, lessonEntity)
                .leftJoin(reservation).on(schedule.id.eq(reservation.schedule.id).and(reservation.user.id.eq(userId)))
                .where(schedule.lesson.id.eq(lessonId),
                        scheduleEntity.scheduleStart.between(now, sevenDaysLater))
                .fetch();
    }
}
