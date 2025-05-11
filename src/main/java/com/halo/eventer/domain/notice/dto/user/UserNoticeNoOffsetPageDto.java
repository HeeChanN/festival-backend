package com.halo.eventer.domain.notice.dto.user;

import java.util.List;
import java.util.stream.Collectors;

import com.halo.eventer.domain.notice.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserNoticeNoOffsetPageDto {
    private List<UserNoticeSummaryDto> notices;
    private Boolean isLast;

    @Builder
    private UserNoticeNoOffsetPageDto(List<Notice> notices, Boolean isLast) {
        this.notices = notices.stream().map(UserNoticeSummaryDto::from).collect(Collectors.toList());
        this.isLast = isLast;
    }

    public static UserNoticeNoOffsetPageDto of(List<Notice> notices, Boolean isLast) {
        return UserNoticeNoOffsetPageDto.builder()
                .notices(notices)
                .isLast(isLast)
                .build();
    }
}
