package com.halo.eventer.global.security;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.halo.eventer.domain.stamp.StampUser;

public class CustomStampUserDetails implements UserDetails {

    private final StampUser stampUser;

    public CustomStampUserDetails(StampUser stampUser) {
        this.stampUser = stampUser;
    }

    @Override
    public String getUsername() {
        return stampUser.getUuid();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of("ROLE_STAMP").map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        // 샘플 코드
        //                member.getAuthorities().stream()
        //                .map(o -> new SimpleGrantedAuthority(o.getRoleName()))
        //                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
