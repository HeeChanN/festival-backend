package com.halo.eventer.domain.missing_person.formatter;

import com.halo.eventer.domain.missing_person.dto.MissingPersonReqDto;
import com.halo.eventer.infra.naver.sms.template.SmsMessageTemplate;

public class MissingPersonAlertMessageTemplate implements SmsMessageTemplate<MissingPersonReqDto> {

    private String missingPersonTemplate =
            "< 실종자 등록 알림 안내 >\n"
                    + "이름 : %s\n"
                    + "연령대 : %s\n"
                    + "성별 : %s\n"
                    + "실종 위치 : %s\n\n"
                    + "자세한 실종자 정보는 링크 확인\n"
                    + "%s";

    @Override
    public String buildMessage(MissingPersonReqDto payload) {
        return String.format(
                missingPersonTemplate,
                defaultString(payload.getName()),
                defaultString(payload.getAge()),
                defaultString(payload.getGender()),
                defaultString(payload.getMissingLocation()),
                defaultString(payload.getDomainUrlName())
        );
    }

    private String defaultString(String value) {
        return value != null ? value : "-";
    }
}
