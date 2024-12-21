package com.join.lesson.feature.reservation.repository.web;

import com.join.lesson.feature.reservation.dto.CanceledReservationResponse;
import com.join.lesson.feature.reservation.dto.ReservationResponse;
import com.join.lesson.feature.reservation.dto.ReservedMembersResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.join.lesson.core.entity.web.QReservationEntity.reservationEntity;
import static com.join.lesson.core.entity.web.QScheduleEntity.scheduleEntity;
import static com.join.lesson.core.entity.web.QUserEntity.userEntity;
import static com.join.lesson.core.entity.web.QLessonEntity.lessonEntity;


@Repository
public class ReservationQueryRepository {
    private final JPAQueryFactory queryFactory;

    public ReservationQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public boolean existReserved(Long membeeId, Long scheduleId) {

        return queryFactory
                .from(reservationEntity)
                .where(reservationEntity.user.id.eq(membeeId),
                        reservationEntity.schedule.id.eq(scheduleId))
                .fetchCount() > 0;
    }

    public List<ReservationResponse> getReservation(Long memberId) {
        return queryFactory
                .select(Projections.constructor(ReservationResponse.class,
                        reservationEntity.id.as("reservationId"),
                        reservationEntity.schedule.id.as("scheduleId"),
                        reservationEntity.state.as("state"),
                        reservationEntity.schedule.scheduleName.as("title"),
                        reservationEntity.schedule.scheduleStart.as("start"),
                        reservationEntity.schedule.scheduleEnd.as("end"),
                        reservationEntity.schedule.lesson.id,
                        reservationEntity.schedule.lesson.lessonName
                ))
                .from(reservationEntity)
                .join(reservationEntity.schedule, scheduleEntity)
                .where(reservationEntity.user.id.eq(memberId),
                        reservationEntity.state.eq(3).not().or(reservationEntity.state.eq(4).not()))
                .fetch();
    }

    public List<CanceledReservationResponse> getCanceledReservationForPro(Long lessonId) {
        return queryFactory
                .select(Projections.constructor(CanceledReservationResponse.class,
                        reservationEntity.id,
                        reservationEntity.cancelReason,
                        reservationEntity.user.name,
                        reservationEntity.user.loginId,
                        reservationEntity.schedule.scheduleName,
                        reservationEntity.schedule.scheduleStart,
                        reservationEntity.schedule.scheduleEnd
                ))
                .from(reservationEntity)
                .join(reservationEntity.user, userEntity)
                .join(reservationEntity.schedule, scheduleEntity)
                .where(reservationEntity.lessonId.eq(lessonId),
                        reservationEntity.state.eq(2))
                .fetch();
    }

    public List<ReservedMembersResponse> getReservedMembers(Long scheduleId) {
        return queryFactory
                .select(Projections.constructor(ReservedMembersResponse.class,
                        reservationEntity.user.id,
                        reservationEntity.user.loginId,
                        reservationEntity.user.name,
                        reservationEntity.user.tel
                ))
                .from(reservationEntity)
                .join(reservationEntity.user, userEntity)
                .where(reservationEntity.schedule.id.eq(scheduleId))
                .fetch();
    }

    public List<ReservationResponse> getReservationHistory(Long memberId) {
        return createReservationResponseQuery()
                .where(reservationEntity.state.eq(1).not(),
                        reservationEntity.state.eq(3).not(),
                        reservationEntity.user.id.eq(memberId))
                .fetch();
    }

    public boolean isScheduleAvailable(LocalDateTime startTime, LocalDateTime endTime, Long memberId) {
        long count = queryFactory
                .selectFrom(reservationEntity)
                .join(reservationEntity.schedule, scheduleEntity)
                .where(
                        reservationEntity.schedule.scheduleStart.before(endTime),
                        reservationEntity.schedule.scheduleEnd.after(startTime),
                        reservationEntity.state.ne(3),
                        reservationEntity.user.id.eq(memberId)
                )
                .fetchCount();

        return count == 0;
    }

    public List<ReservationResponse> getPrivateLessonSchedules(Long memberId, Long lessonId) {
        return createReservationResponseQuery()
                .where(reservationEntity.user.id.eq(memberId),
                        reservationEntity.lessonId.eq(lessonId))
                .fetch();
    }

    public List<ReservationResponse> getPrivateLessonScheduleApplications(Long lessonId) {
        return createReservationResponseQuery()
                .where(reservationEntity.state.eq(4),
                        reservationEntity.lessonId.eq(lessonId))
                .fetch();
    }

    public List<ReservationResponse> getPrivateLessonScheduleChangeApplications(Long lessonId) {
        return createReservationResponseQuery()
                .where(reservationEntity.state.eq(5),
                        reservationEntity.lessonId.eq(lessonId))
                .fetch();
    }

    private JPAQuery<ReservationResponse> createReservationResponseQuery() {
        return queryFactory
                .select(Projections.constructor(ReservationResponse.class,
                        reservationEntity.id,
                        reservationEntity.schedule.id,
                        reservationEntity.state,
                        reservationEntity.schedule.scheduleName,
                        reservationEntity.schedule.scheduleStart,
                        reservationEntity.schedule.scheduleEnd,
                        reservationEntity.schedule.lesson.id,
                        reservationEntity.schedule.lesson.lessonName
                ))
                .from(reservationEntity)
                .join(reservationEntity.schedule, scheduleEntity);
    }
}
