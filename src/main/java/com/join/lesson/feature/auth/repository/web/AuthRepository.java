package com.join.lesson.feature.auth.repository.web;

import com.join.lesson.core.entity.web.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByLoginId(String loginId);
}
