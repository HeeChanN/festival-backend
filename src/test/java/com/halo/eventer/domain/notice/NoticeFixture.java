package com.halo.eventer.domain.notice;

import java.util.List;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.notice.dto.NoticeCreateReqDto;
import com.halo.eventer.domain.notice.dto.NoticeUpdateReqDto;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class NoticeFixture {

    public static NoticeCreateReqDto 공지사항_생성_DTO() {
        NoticeCreateReqDto noticeCreateReqDto = new NoticeCreateReqDto();
        setField(noticeCreateReqDto, "title", "title");
        setField(noticeCreateReqDto, "content", "content");
        setField(noticeCreateReqDto, "tag", "tag");
        setField(noticeCreateReqDto, "writer", "writer");
        setField(noticeCreateReqDto, "thumbnail", "thumbnail");
        setField(noticeCreateReqDto, "type", ArticleType.NOTICE);
        setField(noticeCreateReqDto, "images", List.of("image1", "image2", "image3"));
        return noticeCreateReqDto;
    }

    public static NoticeUpdateReqDto 공지사항_수정_DTO() {
        NoticeUpdateReqDto noticeUpdateReqDto = new NoticeUpdateReqDto();
        setField(noticeUpdateReqDto, "title", "changedTitle");
        setField(noticeUpdateReqDto, "content", "changedContent");
        setField(noticeUpdateReqDto, "tag", "changedTag");
        setField(noticeUpdateReqDto, "writer", "changedWriter");
        setField(noticeUpdateReqDto, "thumbnail", "changedThumbnail");
        setField(noticeUpdateReqDto, "type", ArticleType.EVENT);
        setField(noticeUpdateReqDto, "images", List.of("image4", "image5", "image6"));
        return noticeUpdateReqDto;
    }

    public static Notice 공지사항_엔티티() {
        Festival festival = FestivalFixture.축제_엔티티();
        return Notice.from(festival, 공지사항_생성_DTO());
    }
}
