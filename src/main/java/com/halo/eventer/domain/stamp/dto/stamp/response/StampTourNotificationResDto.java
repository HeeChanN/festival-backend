package com.halo.eventer.domain.stamp.dto.stamp.response;

import com.halo.eventer.domain.stamp.StampNotice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampTourNotificationResDto {
    private String cautionContent;
    private String personalInformationContent;

    public static StampTourNotificationResDto from(StampNotice stampNotice) {
        return new StampTourNotificationResDto(
                stampNotice.getCautionContent(), stampNotice.getPersonalInformationContent());
    }
}
