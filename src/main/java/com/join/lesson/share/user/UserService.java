package com.join.lesson.share.user;

import com.join.lesson.core.entity.web.UserEntity;

public interface UserService {

    UserEntity getUserByLoginId(String loingId);

    UserEntity getUserById(Long userId);
}
