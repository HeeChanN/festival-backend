package com.halo.eventer.domain.widget.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.widget.WidgetFixture;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetCreateDto;
import com.halo.eventer.domain.widget.entity.MiddleWidget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SuppressWarnings("NonAsciiCharacters")
@Transactional
public class MiddleWidgetRepositoryTest {

    @Autowired
    private MiddleWidgetRepository middleWidgetRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private Festival festival;
    private MiddleWidgetCreateDto middleWidgetCreateDto;

    @BeforeEach
    void setUp() {
        festival = festivalRepository.save(FestivalFixture.축제_엔티티());
        middleWidgetCreateDto = WidgetFixture.중간_위젯_생성_DTO();
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE base_widget ");
        jdbcTemplate.execute("TRUNCATE TABLE festival ");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    void MiddleWidget_리스트_festivalId_로_조회() {
        // given
        saveDownWidget(4);

        // when
        List<MiddleWidget> middleWidgets = middleWidgetRepository.findAllByFestivalId(festival.getId());

        // then
        assertThat(middleWidgets).hasSize(4);
    }

    private void saveDownWidget(int count) {
        for (int i = 0; i < count; i++) {
            MiddleWidget mainWidget = WidgetFixture.중간_위젯_엔티티(festival, middleWidgetCreateDto);
            setField(mainWidget, "updatedAt", LocalDateTime.now());
            middleWidgetRepository.save(mainWidget);
        }
    }
}
