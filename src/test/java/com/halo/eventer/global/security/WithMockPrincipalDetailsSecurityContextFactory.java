package com.halo.eventer.global.security;

import java.util.Arrays;
import java.util.Collections;

import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.halo.eventer.domain.member.Member;

public class WithMockPrincipalDetailsSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUserDetails> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUserDetails annotation) {

        CustomUserDetails customUserDetails = Mockito.mock(CustomUserDetails.class);
        Member member = Mockito.mock(Member.class);

        Mockito.when(member.getId()).thenReturn(annotation.id());
        Mockito.when(member.getLoginId()).thenReturn("admin");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + Arrays.toString(annotation.roles()))));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
        return context;
    }
}
