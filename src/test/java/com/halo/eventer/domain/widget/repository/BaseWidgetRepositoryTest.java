package com.halo.eventer.domain.widget.repository;

import java.time.LocalDateTime;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.widget.WidgetFixture;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetCreateDto;
import com.halo.eventer.domain.widget.entity.MainWidget;
import com.halo.eventer.global.common.BaseTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SuppressWarnings("NonAsciiCharacters")
@Transactional
public class BaseWidgetRepositoryTest {

    @Autowired
    private BaseWidgetRepository baseWidgetRepository;

    @Autowired
    private MainWidgetRepository mainWidgetRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    private Festival festival;
    private MainWidgetCreateDto mainWidgetCreateDto;

    @BeforeEach
    void setUp() {
        festival = festival = festivalRepository.save(FestivalFixture.축제_엔티티());
        mainWidgetCreateDto = WidgetFixture.메인_위젯_생성_DTO();
    }

    @Test
    void MainWidget_페이지_createdAt_desc_로_정렬_조회() {
        saveDownWidget(5);
        Page<MainWidget> page =
                baseWidgetRepository.findChildCreateDesc(MainWidget.class, festival.getId(), PageRequest.of(0, 2));

        assertThat(page.getContent())
                .extracting(BaseTime::getCreatedAt)
                .isSortedAccordingTo(Comparator.reverseOrder()) // 내림차순
                .hasSize(2);
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    void MainWidget_페이지_updatedAt_desc_로_정렬_조회() {
        saveDownWidget(5);
        Page<MainWidget> page =
                baseWidgetRepository.findChildUpdateDesc(MainWidget.class, festival.getId(), PageRequest.of(0, 2));

        assertThat(page.getContent())
                .extracting(BaseTime::getUpdatedAt)
                .isSortedAccordingTo(Comparator.reverseOrder())
                .hasSize(2);
        assertThat(page.hasNext()).isTrue();
    }

    private void saveDownWidget(int count) {
        for (int i = 0; i < count; i++) {
            MainWidget mainWidget = WidgetFixture.메인_위젯_엔티티(festival, mainWidgetCreateDto);
            setField(mainWidget, "updatedAt", LocalDateTime.now());
            setField(mainWidget, "createdAt", LocalDateTime.now());
            mainWidgetRepository.save(mainWidget);
        }
    }
}
