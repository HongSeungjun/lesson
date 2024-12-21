package com.join.lesson.testUser;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomAccountSecurityContextFactory.class)
public @interface WithMockCustomAccount {

    String loginId() default "test";

    String name() default "test";

    String tel() default "01012341234";

    String role() default "ROLE_PRO";
}
