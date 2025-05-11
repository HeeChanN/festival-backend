package com.halo.eventer.domain.festival.dto;

import com.halo.eventer.domain.festival.Festival;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalResDto {
    private Long id;

    private String name;

    private String logo;

    private ColorDto colors;

    private double latitude; // 위도
    private double longitude; // 경도

    public FestivalResDto(
            final Long id,
            final String name,
            final String logo,
            final ColorDto colors,
            final double latitude,
            final double longitude) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.colors = colors;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static FestivalResDto from(final Festival festival) {
        final ColorDto colorDto = ColorDto.from(festival);
        return new FestivalResDto(
                festival.getId(),
                festival.getName(),
                festival.getLogo(),
                colorDto,
                festival.getLatitude(),
                festival.getLongitude());
    }
}
