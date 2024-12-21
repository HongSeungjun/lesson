package com.join.lesson.feature.user;

import com.join.lesson.core.entity.web.UserEntity;
import com.join.lesson.core.common.exception.BaseException;
import com.join.lesson.core.common.exception.ErrorCode;
import com.join.lesson.feature.user.dto.UpdateUserRequset;
import com.join.lesson.feature.user.dto.UserResponse;
import com.join.lesson.feature.user.repository.web.UserQueryRepository;
import com.join.lesson.feature.user.repository.web.UserRepository;
import com.join.lesson.share.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserUseCase implements UserService {

    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;

    public UserResponse getUserInfo(Long userId) {

        return userQueryRepository.getUserInfo(userId);

    }

    @Transactional
    public Long updateUserInfo(UpdateUserRequset updateUserRequset, Long userId) {

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
        user.changeInfo(updateUserRequset);

        return user.getId();
    }

    @Override
    public UserEntity getUserByLoginId(String loingId) {

        UserEntity user = userRepository.findByLoginId(loingId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
        return user;

    }

    @Override
    public UserEntity getUserById(Long userId) {
        return userRepository.getReferenceById(userId);
    }

}

