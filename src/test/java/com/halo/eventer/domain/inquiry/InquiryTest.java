package com.halo.eventer.domain.inquiry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.inquiry.dto.InquiryCreateReqDto;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class InquiryTest {

    private Festival festival;
    private Inquiry inquiry;
    private InquiryCreateReqDto inquiryCreateReqDto;

    @BeforeEach
    void setUp() {
        Festival festival = new Festival();
        inquiryCreateReqDto = new InquiryCreateReqDto("제목", false, "아이디", "1234", "문의내용");
        inquiry = new Inquiry(festival, inquiryCreateReqDto, "1234");
    }

    @Test
    void 문의객체_생성() {
        // when
        Inquiry target = new Inquiry(festival, inquiryCreateReqDto, "1234");

        // then
        assertThat(target)
                .usingRecursiveComparison()
                .comparingOnlyFields("title", "userId", "content", "isSecret", "password")
                .isEqualTo(inquiry);
    }

    @Test
    void 답변등록() {
        // given
        String answer = "답변";

        // when
        inquiry.registerAnswer(answer);

        // then
        assertThat(inquiry.getAnswer()).isEqualTo(answer);
    }
}
