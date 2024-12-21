package com.join.lesson.feature.lesson.repository.web;

import com.join.lesson.feature.lesson.dto.LessonResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.join.lesson.core.entity.web.QLessonEntity.*;


@Repository
public class LessonQueryRepository {

    private final JPAQueryFactory queryFactory;

    public LessonQueryRepository(
            EntityManager em
    ) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<LessonResponse> getLesson(Long userId) {
        return queryFactory
                .select(Projections.constructor(LessonResponse.class,
                        lessonEntity.id,
                        lessonEntity.lessonName,
                        lessonEntity.lessonType,
                        lessonEntity.lessonProperty
                ))
                .from(lessonEntity)
                .where(lessonEntity.userEntity.id.eq(userId))
                .fetch();
    }

    private BooleanExpression eqLessonType(Integer type) {
        return type != null ? lessonEntity.lessonType.eq(type) : null;
    }
}
