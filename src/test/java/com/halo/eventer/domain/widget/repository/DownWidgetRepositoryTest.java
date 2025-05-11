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
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetCreateDto;
import com.halo.eventer.domain.widget.entity.DownWidget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SuppressWarnings("NonAsciiCharacters")
@Transactional
public class DownWidgetRepositoryTest {

    @Autowired
    private DownWidgetRepository downWidgetRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private Festival festival;
    private DownWidgetCreateDto downWidgetCreateDto;

    @BeforeEach
    void setUp() {
        festival = festivalRepository.save(FestivalFixture.축제_엔티티());
        downWidgetCreateDto = WidgetFixture.하단_위젯_생성_DTO();
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE base_widget ");
        jdbcTemplate.execute("TRUNCATE TABLE festival ");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    void DownWidget_리스트_festivalId_로_조회() {
        // given
        saveDownWidget(3);

        // when
        List<DownWidget> downWidgets = downWidgetRepository.findAllByFestivalId(festival.getId());

        // then
        assertThat(downWidgets).hasSize(3);
    }

    private void saveDownWidget(int count) {
        for (int i = 0; i < count; i++) {
            DownWidget downWidget = WidgetFixture.하단_위젯_엔티티(festival, downWidgetCreateDto);
            setField(downWidget, "updatedAt", LocalDateTime.now());
            downWidgetRepository.save(downWidget);
        }
    }
}
