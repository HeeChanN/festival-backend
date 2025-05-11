package com.halo.eventer.domain.notice.repository;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.notice.ArticleType;
import com.halo.eventer.domain.notice.Notice;
import com.halo.eventer.domain.notice.NoticeFixture;
import com.halo.eventer.domain.notice.dto.NoticeCreateReqDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SuppressWarnings("NonAsciiCharacters")
@Transactional
public class NoticeRepositoryTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2025-05-07T12:00:00Z"), ZoneId.of("Asia/Seoul"));

    private Festival festival;
    private NoticeCreateReqDto noticeCreateReqDto;

    @BeforeEach
    void setUp() {
        festival = festivalRepository.save(FestivalFixture.축제_엔티티());
        noticeCreateReqDto = NoticeFixture.공지사항_생성_DTO();
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE notice ");
        jdbcTemplate.execute("TRUNCATE TABLE festival ");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    void picked_notice_리스트_조회() {
        // given
        savePickedNotice(3);

        // when
        List<Notice> result = noticeRepository.findAllByPickedAndFestivalId(festival.getId(), true);

        // then
        assertThat(result).hasSize(3);
        assertThat(result).allMatch(Notice::isPicked);
    }

    @Test
    void offset_페이징으로_조회하기() {
        // given
        savePickedNotice(3);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Notice> page = noticeRepository.findAllByTypeAndFestivalIdOrderByUpdatedAtDesc(
                ArticleType.NOTICE, festival.getId(), pageable);

        // then
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getContent().get(0).getUpdatedAt())
                .isAfterOrEqualTo(page.getContent().get(2).getUpdatedAt());
    }

    @Test
    void index_기반_페이징_첫_페이지_조회() {
        // given
        LocalDateTime yesterday = LocalDateTime.now(FIXED_CLOCK).minusDays(1);
        saveNotice(3, yesterday);
        LocalDateTime oneHourAgo = LocalDateTime.now(FIXED_CLOCK).minusHours(1);
        saveNotice(4, oneHourAgo);

        // when
        List<Notice> firstPage = noticeRepository.getNoticesNextPageByFestivalIdAndLastValue(
                festival.getId(), ArticleType.NOTICE.name(), LocalDateTime.of(9999, 12, 31, 23, 59, 59), 0L, 5);

        // then
        assertThat(firstPage).hasSize(5);
        // 3번째 인덱스(네 번째 요소)는 oneHourAgo
        assertThat(firstPage.get(3).getUpdatedAt()).isEqualTo(oneHourAgo);
    }

    @Test
    void index_기반_페이징_N번째_페이지_조회() {
        // given
        LocalDateTime yesterday = LocalDateTime.now(FIXED_CLOCK).minusDays(1);
        saveNotice(3, yesterday);
        LocalDateTime twoHourAgo = LocalDateTime.now(FIXED_CLOCK).minusHours(2);
        saveNotice(3, twoHourAgo);
        Notice n = Notice.from(festival, noticeCreateReqDto);
        setField(n, "updatedAt", twoHourAgo);
        setField(n, "createdAt", twoHourAgo);
        noticeRepository.save(n);
        Notice noticeId7 =
                noticeRepository.findById(n.getId()).orElseThrow(() -> new IllegalStateException("ID 7 not found"));
        LocalDateTime threeHourAgo = LocalDateTime.now(FIXED_CLOCK).minusHours(3);
        setField(noticeId7, "updatedAt", threeHourAgo);
        noticeRepository.flush();

        List<Notice> firstPage = noticeRepository.getNoticesNextPageByFestivalIdAndLastValue(
                festival.getId(), ArticleType.NOTICE.name(), LocalDateTime.of(9999, 12, 31, 23, 59, 59), 0L, 5);

        LocalDateTime cursorTime = firstPage.get(4).getUpdatedAt();
        Long cursorId = firstPage.get(4).getId();

        // when
        List<Notice> nextPage = noticeRepository.getNoticesNextPageByFestivalIdAndLastValue(
                festival.getId(), ArticleType.NOTICE.name(), cursorTime, cursorId, 5);

        // then
        assertThat(nextPage).hasSize(2);
        assertThat(nextPage.get(0).getUpdatedAt()).isEqualTo(cursorTime);
        assertThat(nextPage.get(0).getId()).isGreaterThan(cursorId);
        for (Notice notice : firstPage) {
            System.out.println(notice.getId() + " " + notice.getUpdatedAt());
        }
        for (Notice notice : nextPage) {
            System.out.println(notice.getId() + " " + notice.getUpdatedAt());
        }
    }

    private void savePickedNotice(int count) {
        saveNotice(count, LocalDateTime.now(FIXED_CLOCK), true);
    }

    private void saveNotice(int count, LocalDateTime time) {
        saveNotice(count, time, false);
    }

    private void saveNotice(int count, LocalDateTime time, boolean picked) {
        for (int i = 0; i < count; i++) {
            Notice n = Notice.from(festival, noticeCreateReqDto);
            n.updatePicked(picked);
            setField(n, "updatedAt", time);
            setField(n, "createdAt", time);
            noticeRepository.save(n);
        }
    }
}
