package com.join.lesson.feature.user.repository.web;

import com.join.lesson.feature.user.dto.UserResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.join.lesson.core.entity.web.QUserEntity.*;

@Repository
public class UserQueryRepository {
    private final JPAQueryFactory queryFactory;

    public UserQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public UserResponse getUserInfo(Long userId) {
        return queryFactory
                .select(Projections.constructor(UserResponse.class,
                        userEntity.id,
                        userEntity.name,
                        userEntity.tel,
                        userEntity.comment,
                        userEntity.profileUrl
                ))
                .from(userEntity)
                .where(userEntity.id.eq(userId))
                .fetchOne();
    }
}
