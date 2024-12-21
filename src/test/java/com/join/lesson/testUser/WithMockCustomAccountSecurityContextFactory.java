package com.join.lesson.testUser;

import com.join.lesson.core.entity.web.MemberDetails;
import com.join.lesson.core.entity.web.UserEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomAccountSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomAccount> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomAccount annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        MemberDetails principal = new MemberDetails(UserEntity.builder()
                .loginId(annotation.loginId())
                .name(annotation.name())
                .tel(annotation.tel())
                .active(0)
                .role(annotation.role())
                .build());

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
