package com.join.lesson.feature.reservation.repository.web;

import com.join.lesson.feature.reservation.dto.ReservationChangeResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.join.lesson.core.entity.web.QReservationChangeEntity.reservationChangeEntity;
import static com.join.lesson.core.entity.web.QReservationEntity.reservationEntity;

@Repository
public class ReservationChangeQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ReservationChangeQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ReservationChangeResponse> getPrivateLessonScheduleChangeApplications(Long lessonId) {
        return queryFactory
                .select(Projections.constructor(ReservationChangeResponse.class,
                        reservationChangeEntity.id,
                        reservationChangeEntity.originStart,
                        reservationChangeEntity.originEnd,
                        reservationChangeEntity.requestStart,
                        reservationChangeEntity.requestEnd,
                        reservationChangeEntity.state,
                        reservationChangeEntity.reason,
                        reservationChangeEntity.reservation.id,
                        reservationChangeEntity.unavailableId
                ))
                .from(reservationChangeEntity)
                .join(reservationChangeEntity.reservation, reservationEntity)
                .where(reservationChangeEntity.reservation.lessonId.eq(lessonId)
                        , reservationChangeEntity.state.eq(0))
                .fetch();
    }

}
