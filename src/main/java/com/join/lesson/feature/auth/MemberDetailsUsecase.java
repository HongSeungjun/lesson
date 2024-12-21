package com.join.lesson.feature.auth;

import com.join.lesson.core.entity.web.MemberDetails;
import com.join.lesson.core.entity.web.UserEntity;
import com.join.lesson.feature.auth.repository.web.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsUsecase implements UserDetailsService {

    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) {
        UserEntity user = authRepository.findByLoginId(loginId);
        if (user != null) {
            return new MemberDetails(user);
        }
        return null;
    }

}