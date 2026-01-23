package com.halo.eventer.domain.stamp.dto.stamp.response;

import com.halo.eventer.domain.stamp.dto.stamp.enums.JoinVerificationMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StampTourJoinTemplateResDto {
    private JoinVerificationMethod method;

    public static StampTourJoinTemplateResDto from(JoinVerificationMethod method) {
        return new StampTourJoinTemplateResDto(method);
    }
}
