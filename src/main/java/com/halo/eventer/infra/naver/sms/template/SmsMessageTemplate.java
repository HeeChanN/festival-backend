package com.halo.eventer.infra.naver.sms.template;

public interface SmsMessageTemplate <T>{
    String buildMessage(T payload);
}
