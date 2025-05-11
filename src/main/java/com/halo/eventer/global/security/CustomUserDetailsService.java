package com.halo.eventer.global.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.halo.eventer.domain.member.exception.MemberNotFoundException;
import com.halo.eventer.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) {
        return new CustomUserDetails(memberRepository.findByLoginId(loginId).orElseThrow(MemberNotFoundException::new));
    }
}
