package com.halo.eventer.domain.splash.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.halo.eventer.domain.splash.Splash;

public interface SplashRepository extends JpaRepository<Splash, Long> {
    Optional<Splash> findByFestivalId(long festivalId);
}
