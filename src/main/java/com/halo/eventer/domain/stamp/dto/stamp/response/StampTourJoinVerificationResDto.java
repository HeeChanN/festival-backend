package com.halo.eventer.domain.stamp.dto.stamp.response;

import com.halo.eventer.domain.stamp.dto.stamp.enums.JoinVerificationMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampTourJoinVerificationResDto {
    private JoinVerificationMethod joinVerificationMethod;

    public static StampTourJoinVerificationResDto from(JoinVerificationMethod method) {
        return new StampTourJoinVerificationResDto(method);
    }
}
