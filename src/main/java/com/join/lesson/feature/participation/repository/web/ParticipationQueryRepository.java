package com.join.lesson.feature.participation.repository.web;

import com.join.lesson.feature.participation.dto.GetParticipatedLessonResponse;
import com.join.lesson.feature.participation.dto.ParticipationResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.join.lesson.core.entity.web.QLessonEntity.*;
import static com.join.lesson.core.entity.web.QParticipationEntity.*;
import static com.join.lesson.core.entity.web.QUserEntity.*;


@Repository
public class ParticipationQueryRepository {
    private final JPAQueryFactory queryFactory;

    public ParticipationQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ParticipationResponse> getLessonParticipaion(Long lessonId) {
        return queryFactory
                .select(Projections.constructor(ParticipationResponse.class,
                        participationEntity.id,
                        participationEntity.remainingCount,
                        participationEntity.createdDate,
                        participationEntity.user.id,
                        participationEntity.user.loginId,
                        participationEntity.user.name,
                        participationEntity.user.profileUrl
                ))
                .from(participationEntity)
                .join(participationEntity.user, userEntity)
                .where(participationEntity.lesson.id.eq(lessonId))
                .fetch();
    }

    public List<GetParticipatedLessonResponse> getParticipatedLessonForMember(Long memberId, Integer lessonType) {
        return queryFactory
                .select(Projections.constructor(GetParticipatedLessonResponse.class,
                        participationEntity.lesson.id,
                        participationEntity.lesson.lessonName,
                        participationEntity.lesson.lessonType,
                        participationEntity.lesson.userEntity.id,
                        participationEntity.lesson.userEntity.name.as("proName"),
                        participationEntity.lesson.userEntity.tel.as("proTel"),
                        participationEntity.lesson.userEntity.profileUrl.as("proProfile"),
                        participationEntity.remainingCount
                ))
                .from(participationEntity)
                .join(participationEntity.user, userEntity)
                .join(participationEntity.lesson, lessonEntity)
                .where(participationEntity.user.id.eq(memberId),
                        eqLessonType(lessonType))
                .fetch();
    }

    public int getRemainingCount(Long memberId, Long lessonId) {
        return queryFactory
                .select(participationEntity.remainingCount)
                .from(participationEntity)
                .where(participationEntity.user.id.eq(memberId),
                        participationEntity.lesson.id.eq(lessonId))
                .fetchOne();
    }

    public boolean exists(Long memberId, Long lessonId) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(participationEntity)
                .where(participationEntity.user.id.eq(memberId),
                        participationEntity.lesson.id.eq(lessonId))
                .fetchFirst();
        return fetchOne != null;
    }

    private BooleanExpression eqLessonType(Integer lessonType) {
        return lessonType != null ? lessonEntity.lessonType.eq(lessonType) : null;
    }
}
