package com.halo.eventer.domain.stamp.dto.stamp.request;

import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.dto.stamp.enums.JoinVerificationMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampTourSignUpTemplateResDto {
    private JoinVerificationMethod joinVerificationMethod;
    private String extraInfoTemplate;

    public static StampTourSignUpTemplateResDto from(Stamp stamp) {
        return new StampTourSignUpTemplateResDto(stamp.getJoinVerificationMethod(), stamp.getExtraInfoTemplate());
    }
}
