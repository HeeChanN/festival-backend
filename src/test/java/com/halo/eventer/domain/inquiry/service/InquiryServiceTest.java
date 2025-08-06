package com.halo.eventer.domain.inquiry.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.inquiry.Inquiry;
import com.halo.eventer.domain.inquiry.InquiryConstants;
import com.halo.eventer.domain.inquiry.dto.*;
import com.halo.eventer.domain.inquiry.exception.InquiryNotFoundException;
import com.halo.eventer.domain.inquiry.exception.InquiryUnauthorizedAccessException;
import com.halo.eventer.domain.inquiry.repository.InquiryRepository;
import com.halo.eventer.global.security.PasswordService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class InquiryServiceTest {
    @Mock
    private InquiryRepository inquiryRepository;

    @Mock
    private PasswordService passwordService;

    @Mock
    private FestivalRepository festivalRepository;

    @InjectMocks
    private InquiryService inquiryService;

    private Inquiry secretInquiry;
    private Inquiry unSecretInquiry;
    private Festival festival;
    private InquiryCreateReqDto secretInquiryCreateReqDto;
    private InquiryCreateReqDto unSecretInquiryCreateReqDto;
    private final Long inquiryId = 1L;
    private final Long festivalId = 1L;

    @BeforeEach
    void setUp() {
        secretInquiryCreateReqDto = new InquiryCreateReqDto("제목", true, "aaa", "1234", "내용");
        unSecretInquiryCreateReqDto = new InquiryCreateReqDto("제목", false, "aaa", "1234", "내용");
        festival = new Festival();
        secretInquiry = new Inquiry(festival, secretInquiryCreateReqDto, "password");
        unSecretInquiry = new Inquiry(festival, unSecretInquiryCreateReqDto, "password");
    }

    @Test
    void 문의생성_성공() {
        // given
        given(passwordService.encode(anyString())).willReturn("1234");
        given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
        given(inquiryRepository.save(any())).willReturn(secretInquiry);

        // when
        inquiryService.create(inquiryId, secretInquiryCreateReqDto);

        // then
        verify(inquiryRepository, times(1)).save(any(Inquiry.class));
    }

    @Test
    void 축제가_없는_경우_문의생성_실패() {
        // given
        given(festivalRepository.findById(festivalId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> inquiryService.create(festivalId, secretInquiryCreateReqDto))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 관리자용_문의_전체조회() {
        // given
        List<Inquiry> inquiries = createMockInquiries(20);
        given(inquiryRepository.getPageByFestivalIdAndLastId(festivalId, 0L, InquiryConstants.INQUIRY_PAGE_SIZE + 1))
                .willReturn(inquiries);

        // when
        InquiryNoOffsetPageDto results = inquiryService.getAllInquiryForAdmin(festivalId, 0L);

        // then
        assertThat(results.getInquiries()).hasSize(20);
        assertThat(results.getInquiries().get(0))
                .usingRecursiveComparison()
                .comparingOnlyFields("title", "isSecret", "isAnswered", "userId")
                .isEqualTo(secretInquiry);
    }

    @Test
    void 관리자용_문의_전체조회_빈리스트_반환() {
        // given
        given(inquiryRepository.getPageByFestivalIdAndLastId(festivalId, 0L, InquiryConstants.INQUIRY_PAGE_SIZE + 1))
                .willReturn(List.of());

        // when
        InquiryNoOffsetPageDto results = inquiryService.getAllInquiryForAdmin(festivalId, 0L);

        // then
        assertThat(results.getInquiries()).isEmpty();
    }

    @Test
    void 관리자용_문의_단일조회() {
        // given
        given(inquiryRepository.findById(inquiryId)).willReturn(Optional.of(secretInquiry));

        // when
        InquiryResDto result = inquiryService.findInquiryForAdmin(inquiryId);

        // then
        assertThat(result.getTitle()).isEqualTo(secretInquiry.getTitle());
    }

    @Test
    void 관리자용_문의_단일조회_문의가_없는_경우() {
        given(inquiryRepository.findById(inquiryId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> inquiryService.findInquiryForAdmin(inquiryId))
                .isInstanceOf(InquiryNotFoundException.class);
    }

    @Test
    void 관리자용_문의_답변등록() {
        // given
        given(inquiryRepository.findById(inquiryId)).willReturn(Optional.of(secretInquiry));
        InquiryAnswerReqDto dto = new InquiryAnswerReqDto("답변");

        // when
        InquiryResDto result = inquiryService.updateInquiryAnswer(inquiryId, dto);

        // then
        assertThat(result.getAnswer()).isEqualTo(dto.getAnswer());
    }

    @Test
    void 관리자용_문의_삭제() {
        // when
        inquiryService.delete(inquiryId);

        // then
        verify(inquiryRepository, times(1)).deleteById(inquiryId);
    }

    @Test
    void 유저용_문의리스트_조회() {
        // given
        List<Inquiry> inquiries = List.of(secretInquiry, unSecretInquiry);
        given(inquiryRepository.getPageByFestivalIdAndLastId(festivalId, 0L, InquiryConstants.INQUIRY_PAGE_SIZE + 1))
                .willReturn(inquiries);

        // when
        InquiryNoOffsetPageDto results = inquiryService.getAllInquiryForUser(festivalId, 0L);

        // then
        assertThat(results.getInquiries()).hasSize(2);
        assertThat(results.getInquiries().get(0).getTitle()).isEqualTo("HIDDEN");
        assertThat(results.getInquiries().get(1).getTitle()).isEqualTo("제목");
    }

    @Test
    void 유저용_단일문의_비밀글_조회() {
        // given
        given(inquiryRepository.findById(inquiryId)).willReturn(Optional.of(secretInquiry));
        InquiryUserReqDto dto = new InquiryUserReqDto("aaa", "password");
        given(passwordService.matches(any(), any())).willReturn(true);

        // when
        InquiryResDto inquiryResDto = inquiryService.getInquiryForUser(inquiryId, dto);

        // then
        assertThat(inquiryResDto).isNotNull();
        assertThat(inquiryResDto.getTitle()).isEqualTo("제목");
    }

    @Test
    void 유저용_단일문의_비밀글_접근제한() {
        // given
        given(inquiryRepository.findById(inquiryId)).willReturn(Optional.of(secretInquiry));
        InquiryUserReqDto dto = new InquiryUserReqDto("aaa", "1234");
        given(passwordService.matches(any(), any())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> inquiryService.getInquiryForUser(inquiryId, dto))
                .isInstanceOf(InquiryUnauthorizedAccessException.class);
    }

    @Test
    void 문의_페이징조회_마지막페이지() {
        // given
        List<Inquiry> inquiries = createMockInquiries(20);
        given(inquiryRepository.getPageByFestivalIdAndLastId(festivalId, 0L, InquiryConstants.INQUIRY_PAGE_SIZE + 1))
                .willReturn(inquiries);

        // when
        InquiryNoOffsetPageDto dto = inquiryService.getInquiriesWithNoOffsetPaging(festivalId, 0L);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getInquiries().size()).isEqualTo(20);
        assertThat(dto.getIsLast()).isTrue();
    }

    @Test
    void 문의_페이징조회_중간페이지() {
        List<Inquiry> inquiries = createMockInquiries(21);
        given(inquiryRepository.getPageByFestivalIdAndLastId(festivalId, 0L, InquiryConstants.INQUIRY_PAGE_SIZE + 1))
                .willReturn(inquiries);

        // when
        InquiryNoOffsetPageDto dto = inquiryService.getInquiriesWithNoOffsetPaging(festivalId, 0L);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getInquiries().size()).isEqualTo(20);
        assertThat(dto.getIsLast()).isFalse();
    }

    private List<Inquiry> createMockInquiries(int count) {
        List<Inquiry> inquiries = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            inquiries.add(secretInquiry);
        }
        return inquiries;
    }
}
