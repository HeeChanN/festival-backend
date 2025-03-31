package com.halo.eventer.infra.common.sms;

import java.util.List;

public interface SmsClient {
    void send(String message, List<String> phoneNumbers);
}
