package com.halo.eventer.domain.inquiry.repository;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.dto.FestivalCreateDto;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.inquiry.Inquiry;
import com.halo.eventer.domain.inquiry.dto.InquiryCreateReqDto;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Disabled()
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SuppressWarnings("NonAsciiCharacters")
public class InquiryRepositoryTest {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    private Inquiry inquiry;
    private InquiryCreateReqDto secretInquiryCreateReqDto;
    private Festival festival;

    @BeforeEach
    void setUp() {
        FestivalCreateDto dto = new FestivalCreateDto("축제", "univ");
        festival = festivalRepository.save(Festival.from(dto));
        addInquiries();
    }

    @Test
    void 축제ID로_문의_전체조회() {
        // when
        List<Inquiry> results = inquiryRepository.findAllByFestivalId(festival.getId());
        System.out.println(festival.getId());

        // then
        assertThat(results).hasSize(10);
        assertThat(results.get(9).getTitle()).isEqualTo("제목_9");
    }

    @Test
    void 문의_기본_페이징_조회() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));

        // when
        Page<Inquiry> results = inquiryRepository.findAllByFestivalId(festival.getId(), pageRequest);

        // then
        assertThat(results).hasSize(5);
        assertThat(results.getTotalPages()).isEqualTo(2);
        assertThat(results.getTotalElements()).isEqualTo(10);
    }

    @Test
    void 문의_인덱스기반_첫번째_페이지_조회() {
        // when
        List<Inquiry> firstPage = inquiryRepository.getPageByFestivalIdAndLastId(festival.getId(), 0L, 5);

        // then
        assertThat(firstPage).hasSize(5);
        assertThat(firstPage.get(0).getTitle()).isEqualTo("제목_9");
    }

    @Test
    void 문의_인덱스기반_N번째_페이지_조회() {
        // given
        List<Inquiry> firstPage = inquiryRepository.getPageByFestivalIdAndLastId(festival.getId(), 0L, 5);
        Long lastId = firstPage.get(4).getId();

        // when
        List<Inquiry> secondPage = inquiryRepository.getPageByFestivalIdAndLastId(festival.getId(), lastId, 10);
        assertThat(secondPage).hasSize(5);
        assertThat(secondPage.get(0).getTitle()).isEqualTo("제목_4");
    }

    private void addInquiries() {
        for (int i = 0; i < 10; i++) {
            secretInquiryCreateReqDto = new InquiryCreateReqDto(String.format("제목_%d", i), true, "aaa", "1234", "내용");
            inquiry = new Inquiry(festival, secretInquiryCreateReqDto, "password");
            inquiryRepository.save(inquiry);
        }
    }
}
