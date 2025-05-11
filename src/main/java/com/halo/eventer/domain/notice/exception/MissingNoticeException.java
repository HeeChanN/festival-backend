package com.halo.eventer.domain.notice.exception;

import com.halo.eventer.global.error.exception.InvalidInputException;

public class MissingNoticeException extends InvalidInputException {
    public MissingNoticeException(Long noticeId) {
        super("Required Notice is missing in request. id=" + noticeId);
    }
}
