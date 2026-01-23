package com.halo.eventer.domain.stamp.dto.stamp.response;

import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.dto.stamp.enums.AuthMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampTourSettingBasicResDto {
    private Long stampId;
    private Boolean stampActive;
    private String title;
    private AuthMethod authMethod;
    private String prizeReceiptAuthPassword;
    private String mainColor;
    private String subColor;
    private String backgroundColor;
    private String backgroundSubColor;

    public static StampTourSettingBasicResDto from(Stamp stamp) {
        return new StampTourSettingBasicResDto(
                stamp.getId(),
                stamp.getActive(),
                stamp.getTitle(),
                stamp.getAuthMethod(),
                stamp.getPrizeReceiptAuthPassword(),
                stamp.getMainColor(),
                stamp.getSubColor(),
                stamp.getBackgroundColor(),
                stamp.getBackgroundSubColor());
    }
}
