package com.halo.eventer.domain.stamp.v2.repository;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.stamp.PageTemplate;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.dto.stamp.enums.PageType;
import com.halo.eventer.domain.stamp.repository.PageTemplateRepository;
import com.halo.eventer.domain.stamp.repository.StampRepository;
import com.halo.eventer.domain.stamp.v2.fixture.PageTemplateFixture;
import com.halo.eventer.domain.stamp.v2.fixture.StampTourFixture;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class PageTemplateRepositoryTest {

    @Autowired
    private PageTemplateRepository pageTemplateRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private StampRepository stampRepository;

    private Festival 축제;
    private Stamp 스탬프;
    private PageTemplate 템플릿;

    @BeforeEach
    void setUp() {
        축제 = FestivalFixture.축제_엔티티();
        스탬프 = StampTourFixture.스탬프투어1_생성(축제);
        템플릿 = PageTemplateFixture.메인페이지_생성(스탬프);

        festivalRepository.save(축제);
        stampRepository.save(스탬프);
        pageTemplateRepository.save(템플릿);
    }

    @Test
    void 스탬프ID와타입으로_페이지템플릿_조회() {
        // when
        Optional<PageTemplate> result = pageTemplateRepository.findFirstByStampIdAndType(스탬프.getId(), 템플릿.getType());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getStamp().getId()).isEqualTo(스탬프.getId());
        assertThat(result.get().getType()).isEqualTo(PageType.MAIN);
    }
}
