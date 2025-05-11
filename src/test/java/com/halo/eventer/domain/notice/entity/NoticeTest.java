package com.halo.eventer.domain.notice.entity;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.image.Image;
import com.halo.eventer.domain.notice.ArticleType;
import com.halo.eventer.domain.notice.Notice;
import com.halo.eventer.domain.notice.NoticeFixture;
import com.halo.eventer.domain.notice.dto.NoticeCreateReqDto;
import com.halo.eventer.domain.notice.dto.NoticeUpdateReqDto;
import com.halo.eventer.domain.notice.exception.MissingNoticeException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class NoticeTest {

    private Festival festival;
    private NoticeCreateReqDto noticeCreateReqDto;
    private NoticeUpdateReqDto noticeUpdateReqDto;
    private Notice notice;

    @BeforeEach
    void setUp() {
        festival = FestivalFixture.축제_엔티티();
        noticeCreateReqDto = NoticeFixture.공지사항_생성_DTO();
        noticeUpdateReqDto = NoticeFixture.공지사항_수정_DTO();
        notice = Notice.from(festival, noticeCreateReqDto);
    }

    @Test
    void notice_생성_테스트() {
        // when
        Notice target = Notice.from(festival, noticeCreateReqDto);

        // then
        assertThat(target).isNotNull();
        assertThat(target)
                .extracting(
                        Notice::getTitle,
                        Notice::getContent,
                        Notice::getTag,
                        Notice::getWriter,
                        Notice::getThumbnail,
                        Notice::getType)
                .containsExactly("title", "content", "tag", "writer", "thumbnail", ArticleType.NOTICE);
        assertThat(target.getFestival()).isEqualTo(festival);
        assertThat(target.getImages()).extracting(Image::getImageUrl).containsExactly("image1", "image2", "image3");
    }

    @Test
    void picked_필드_수정_테스트() {
        // when
        notice.updatePicked(true);

        // then
        assertThat(notice.isPicked()).isEqualTo(true);
    }

    @Test
    void picked_필드_false_변경시_displayOrder_값_11_테스트() {
        // given
        setField(notice, "displayOrder", 1);

        // when
        notice.updatePicked(false);

        // then
        assertThat(notice.isPicked()).isEqualTo(false);
        assertThat(notice.getDisplayOrder()).isEqualTo(11);
    }

    @Test
    void notice_수정_기존이미지_1개_삭제_새로운_이미지_3개_추가() {
        // given
        setField(notice.getImages().get(0), "id", 1L);
        setField(
                noticeUpdateReqDto,
                "deleteIds",
                List.of(notice.getImages().get(0).getId()));

        // when
        notice.updateNotice(noticeUpdateReqDto);

        // then
        assertThat(notice)
                .extracting(
                        Notice::getTitle,
                        Notice::getContent,
                        Notice::getTag,
                        Notice::getWriter,
                        Notice::getThumbnail,
                        Notice::getType)
                .containsExactly(
                        "changedTitle",
                        "changedContent",
                        "changedTag",
                        "changedWriter",
                        "changedThumbnail",
                        ArticleType.EVENT);
        assertThat(notice.getImages()).hasSize(5);
    }

    @Test
    void 순서_변경_성공() {
        // given
        Notice notice1 = Notice.from(festival, noticeCreateReqDto);
        Notice notice2 = Notice.from(festival, noticeCreateReqDto);
        setField(notice1, "id", 1L);
        setField(notice2, "id", 2L);
        List<Notice> notices = List.of(notice1, notice2);
        Map<Long, Integer> newOrders = Map.of(1L, 3, 2L, 4);

        // when
        Notice.reOrderPickedNotices(notices, newOrders);

        // then
        assertThat(notice1.getDisplayOrder()).isEqualTo(3);
        assertThat(notice2.getDisplayOrder()).isEqualTo(4);
    }

    @Test
    void 순서_변경_대상이_request_에없을_경우_예외() {
        // given
        setField(notice, "id", 1L);
        List<Notice> notices = List.of(notice);
        Map<Long, Integer> newOrders = Map.of(2L, 4);

        // when & then
        assertThatThrownBy(() -> Notice.reOrderPickedNotices(notices, newOrders))
                .isInstanceOf(MissingNoticeException.class);
    }
}
