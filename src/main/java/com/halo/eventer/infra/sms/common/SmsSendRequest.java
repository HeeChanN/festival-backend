package com.halo.eventer.infra.sms.common;

import java.util.List;
import java.util.stream.Collectors;

import com.halo.eventer.domain.missing_person.dto.MissingPersonReqDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SmsSendRequest {
    private String to;
    private String message;

    @Builder
    private SmsSendRequest(String to, String message) {
        this.to = to;
        this.message = message;
    }

    public static SmsSendRequest of(String to, String message) {
        return SmsSendRequest.builder().to(to).message(message).build();
    }

    public static SmsSendRequest of(String phoneNumber, MissingPersonReqDto missingPersonReqDto) {
        return SmsSendRequest.builder()
                .to(phoneNumber)
                .message(makeMissingPersonMessage(missingPersonReqDto))
                .build();
    }

    public static List<SmsSendRequest> of(List<String> phoneNumbers, MissingPersonReqDto missingPersonReqDto) {
        return phoneNumbers.stream()
                .map(phoneNumber -> of(phoneNumber, missingPersonReqDto))
                .collect(Collectors.toList());
    }

    private static String makeMissingPersonMessage(MissingPersonReqDto payload) {
        return String.format(
                MessageTemplate.missingPersonTemplate,
                defaultString(payload.getName()),
                defaultString(payload.getAge()),
                defaultString(payload.getGender()),
                defaultString(payload.getMissingLocation()),
                defaultString(payload.getDomainUrlName()));
    }

    private static String defaultString(String value) {
        return value != null ? value : "-";
    }
}
