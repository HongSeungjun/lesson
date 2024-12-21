package com.join.lesson.feature.auth;

import com.join.lesson.core.entity.web.UserEntity;
import com.join.lesson.feature.auth.dto.JoinUserRequest;
import com.join.lesson.feature.user.repository.web.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthUsecase {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    void join(JoinUserRequest joinUserRequest) {
        String rawPassword = joinUserRequest.getLoginPw();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        userRepository.save(UserEntity.toEntity(joinUserRequest, encPassword));
    }
}
