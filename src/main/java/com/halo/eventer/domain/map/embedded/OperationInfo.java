package com.halo.eventer.domain.map.embedded;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.halo.eventer.domain.map.dto.map.OperationInfoDto;
import com.halo.eventer.domain.map.enumtype.OperationTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class OperationInfo {

    private String hours;

    @Enumerated(EnumType.STRING)
    private OperationTime type;

    @Builder
    private OperationInfo(String hours, OperationTime type) {
        this.hours = hours;
        this.type = type;
    }

    public static OperationInfo from(OperationInfoDto operationInfoDto) {
        return OperationInfo.builder()
                .hours(operationInfoDto.getHours())
                .type(operationInfoDto.getType())
                .build();
    }
}
