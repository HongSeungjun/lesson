package com.join.lesson.feature.unavailableTimes.repository.web;

import com.join.lesson.feature.unavailableTimes.dto.UnavailableTimeResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.join.lesson.core.entity.web.QUnavailableTimesEntity.*;

@Repository
public class UnavailableQueryRepository {

    private final JPAQueryFactory queryFactory;

    public UnavailableQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<UnavailableTimeResponse> getUnavailableTimes(Long proId) {
        return queryFactory
                .select(Projections.constructor(UnavailableTimeResponse.class,
                        unavailableTimesEntity.id,
                        unavailableTimesEntity.daysOfWeek,
                        unavailableTimesEntity.startTime,
                        unavailableTimesEntity.endTime
                ))
                .from(unavailableTimesEntity)
                .where(unavailableTimesEntity.userEntity.id.eq(proId))
                .fetch();
    }
}
