package com.halo.eventer.domain.map.dto.map;

import com.halo.eventer.domain.map.embedded.OperationInfo;
import com.halo.eventer.domain.map.enumtype.OperationTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OperationInfoDto {
    private String hours;
    private OperationTime type;

    @Builder
    private OperationInfoDto(String hours, OperationTime type) {
        this.hours = hours;
        this.type = type;
    }

    public static OperationInfoDto from(OperationInfo operationInfo) {
        return OperationInfoDto.builder()
                .hours(operationInfo.getHours())
                .type(operationInfo.getType())
                .build();
    }
}
