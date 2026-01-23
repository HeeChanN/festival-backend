package com.halo.eventer.domain.stamp.v2.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.stamp.repository.ParticipateGuidePageRepository;

@DataJpaTest
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class ParticipateGuidePageRepositoryTest {

    @Autowired
    private ParticipateGuidePageRepository participateGuidePageRepository;
}
