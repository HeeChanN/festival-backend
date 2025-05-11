package com.halo.eventer.domain.notice;

import com.halo.eventer.domain.notice.dto.NoticeResDto;
import com.halo.eventer.domain.notice.dto.user.UserNoticeResDto;

import static org.assertj.core.api.Assertions.assertThat;

public class NoticeTestUtils {

    public static void assertNoticeDtoEqualsNotice(NoticeResDto result, Notice notice) {
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(notice.getId());
        assertThat(result.getTitle()).isEqualTo(notice.getTitle());
        assertThat(result.getContent()).isEqualTo(notice.getContent());
        assertThat(result.getTag()).isEqualTo(notice.getTag());
        assertThat(result.getType()).isEqualTo(notice.getType());
        assertThat(result.getThumbnail()).isEqualTo(notice.getThumbnail());
        assertThat(result.getWriter()).isEqualTo(notice.getWriter());
        assertThat(result.isPick()).isEqualTo(notice.isPicked());
        assertThat(result.getDisplayOrder()).isEqualTo(notice.getDisplayOrder());
        assertThat(result.getCreatedAt()).isEqualTo(notice.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(notice.getUpdatedAt());
        assertThat(result.getImages()).hasSize(notice.getImages().size());
    }

    public static void assertUserNoticeDtoEqualsNotice(UserNoticeResDto result, Notice notice) {
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(notice.getTitle());
        assertThat(result.getContent()).isEqualTo(notice.getContent());
        assertThat(result.getTag()).isEqualTo(notice.getTag());
        assertThat(result.getThumbnail()).isEqualTo(notice.getThumbnail());
        assertThat(result.getWriter()).isEqualTo(notice.getWriter());
        assertThat(result.getCreateAt()).isEqualTo(notice.getCreatedAt());
        assertThat(result.getUpdateAt()).isEqualTo(notice.getUpdatedAt());
        assertThat(result.getImages()).hasSize(notice.getImages().size());
    }
}
