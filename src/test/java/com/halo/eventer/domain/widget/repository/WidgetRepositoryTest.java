package com.halo.eventer.domain.widget.repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.WidgetFixture;
import com.halo.eventer.domain.widget.WidgetType;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetCreateDto;
import com.halo.eventer.global.common.BaseTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SuppressWarnings("NonAsciiCharacters")
@Transactional
public class WidgetRepositoryTest {

    @Autowired
    private WidgetRepository widgetRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private Festival festival;

    @BeforeEach
    void setUp() {
        festival = festivalRepository.save(FestivalFixture.축제_엔티티());
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE widget ");
        jdbcTemplate.execute("TRUNCATE TABLE festival ");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    void MainWidget_리스트_festivalId_로_조회() {
        MainWidgetCreateDto dto = WidgetFixture.메인_위젯_생성_DTO();
        for (int i = 0; i < 3; i++) {
            Widget widget = WidgetFixture.메인_위젯_엔티티(festival, dto);
            setField(widget, "updatedAt", LocalDateTime.now());
            widgetRepository.save(widget);
        }

        List<Widget> result = widgetRepository.findAllByFestivalIdAndWidgetType(festival.getId(), WidgetType.MAIN);
        assertThat(result).hasSize(3);
    }

    @Test
    void MiddleWidget_리스트_festivalId_로_조회() {
        MiddleWidgetCreateDto dto = WidgetFixture.중간_위젯_생성_DTO();
        for (int i = 0; i < 4; i++) {
            Widget widget = WidgetFixture.중간_위젯_엔티티(festival, dto);
            setField(widget, "updatedAt", LocalDateTime.now());
            widgetRepository.save(widget);
        }

        List<Widget> result = widgetRepository.findAllByFestivalIdAndWidgetType(festival.getId(), WidgetType.MIDDLE);
        assertThat(result).hasSize(4);
    }

    @Test
    void SquareWidget_리스트_festivalId_로_조회() {
        SquareWidgetCreateDto dto = WidgetFixture.정사각형_위젯_생성_DTO();
        for (int i = 0; i < 5; i++) {
            Widget widget = WidgetFixture.정사각형_위젯_엔티티(festival, dto);
            setField(widget, "updatedAt", LocalDateTime.now());
            widgetRepository.save(widget);
        }

        List<Widget> result = widgetRepository.findAllByFestivalIdAndWidgetType(festival.getId(), WidgetType.SQUARE);
        assertThat(result).hasSize(5);
    }

    @Test
    void DownWidget_리스트_festivalId_로_조회() {
        DownWidgetCreateDto dto = WidgetFixture.하단_위젯_생성_DTO();
        for (int i = 0; i < 3; i++) {
            Widget widget = WidgetFixture.하단_위젯_엔티티(festival, dto);
            setField(widget, "updatedAt", LocalDateTime.now());
            widgetRepository.save(widget);
        }

        List<Widget> result = widgetRepository.findAllByFestivalIdAndWidgetType(festival.getId(), WidgetType.DOWN);
        assertThat(result).hasSize(3);
    }

    @Test
    void Widget_페이지_createdAt_desc_로_정렬_조회() {
        MainWidgetCreateDto dto = WidgetFixture.메인_위젯_생성_DTO();
        for (int i = 0; i < 5; i++) {
            Widget widget = WidgetFixture.메인_위젯_엔티티(festival, dto);
            setField(widget, "updatedAt", LocalDateTime.now());
            setField(widget, "createdAt", LocalDateTime.now());
            widgetRepository.save(widget);
        }

        Page<Widget> page = widgetRepository.findByFestivalIdAndWidgetTypeOrderByCreatedAtDesc(
                festival.getId(), WidgetType.MAIN, PageRequest.of(0, 2));

        assertThat(page.getContent())
                .extracting(BaseTime::getCreatedAt)
                .isSortedAccordingTo(Comparator.reverseOrder())
                .hasSize(2);
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    void Widget_페이지_updatedAt_desc_로_정렬_조회() {
        MainWidgetCreateDto dto = WidgetFixture.메인_위젯_생성_DTO();
        for (int i = 0; i < 5; i++) {
            Widget widget = WidgetFixture.메인_위젯_엔티티(festival, dto);
            setField(widget, "updatedAt", LocalDateTime.now());
            setField(widget, "createdAt", LocalDateTime.now());
            widgetRepository.save(widget);
        }

        Page<Widget> page = widgetRepository.findByFestivalIdAndWidgetTypeOrderByUpdatedAtDesc(
                festival.getId(), WidgetType.MAIN, PageRequest.of(0, 2));

        assertThat(page.getContent())
                .extracting(BaseTime::getUpdatedAt)
                .isSortedAccordingTo(Comparator.reverseOrder())
                .hasSize(2);
        assertThat(page.hasNext()).isTrue();
    }
}
