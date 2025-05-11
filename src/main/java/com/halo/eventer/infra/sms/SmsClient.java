package com.halo.eventer.infra.sms;

import java.util.List;

import com.halo.eventer.infra.sms.common.SmsSendRequest;
import com.halo.eventer.infra.sms.naver.dto.NaverSmsResDto;

public interface SmsClient {
    NaverSmsResDto sendToMany(List<SmsSendRequest> smsRequests);
}
