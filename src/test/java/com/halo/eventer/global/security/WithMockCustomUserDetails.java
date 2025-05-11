package com.halo.eventer.global.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockPrincipalDetailsSecurityContextFactory.class)
public @interface WithMockCustomUserDetails {
    long id() default 1L;

    String[] roles() default {"ADMIN"};
}
