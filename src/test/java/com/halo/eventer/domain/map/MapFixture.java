package com.halo.eventer.domain.map;

import java.util.ArrayList;
import java.util.List;

import com.halo.eventer.domain.duration.Duration;
import com.halo.eventer.domain.duration.DurationMap;
import com.halo.eventer.domain.map.dto.map.*;
import com.halo.eventer.domain.map.enumtype.OperationTime;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class MapFixture {

    public static MapCreateDto 지도_생성_DTO() {
        OperationInfoDto operationInfoDto = new OperationInfoDto();
        setField(operationInfoDto, "hours", "hours");
        setField(operationInfoDto, "type", OperationTime.all);

        LocationInfoDto locationInfoDto = new LocationInfoDto();
        setField(locationInfoDto, "address", "address");
        setField(locationInfoDto, "latitude", 0);
        setField(locationInfoDto, "longitude", 0);

        ButtonInfoDto buttonInfoDto = new ButtonInfoDto();
        setField(buttonInfoDto, "name", "button");
        setField(buttonInfoDto, "url", "url");
        setField(buttonInfoDto, "image", "image");

        List<Long> durationIdsToAdd = new ArrayList<>(List.of(5L));

        MapCreateDto dto = new MapCreateDto();
        setField(dto, "name", "name");
        setField(dto, "summary", "summary");
        setField(dto, "content", "content");
        setField(dto, "thumbnail", "thumbnail");
        setField(dto, "icon", "icon");
        setField(dto, "operationInfo", operationInfoDto);
        setField(dto, "locationInfo", locationInfoDto);
        setField(dto, "buttonInfo", buttonInfoDto);
        setField(dto, "durationIdsToAdd", durationIdsToAdd);
        return dto;
    }

    public static MapUpdateDto 지도_수정_DTO() {
        OperationInfoDto operationInfoDto = new OperationInfoDto();
        setField(operationInfoDto, "hours", "u_hours");
        setField(operationInfoDto, "type", OperationTime.evening);

        LocationInfoDto locationInfoDto = new LocationInfoDto();
        setField(locationInfoDto, "address", "u_address");
        setField(locationInfoDto, "latitude", 1);
        setField(locationInfoDto, "longitude", 1);

        ButtonInfoDto buttonInfoDto = new ButtonInfoDto();
        setField(buttonInfoDto, "name", "u_button");
        setField(buttonInfoDto, "url", "u_url");
        setField(buttonInfoDto, "image", "u_image");
        DurationBindingDto durationBindingDto = new DurationBindingDto();
        List<Long> idsToAdd = new ArrayList<>(List.of(5L));
        List<Long> idsToRemove = new ArrayList<>(List.of(1L));
        setField(durationBindingDto, "idsToAdd", idsToAdd);
        setField(durationBindingDto, "idsToRemove", idsToRemove);

        MapUpdateDto dto = new MapUpdateDto();
        setField(dto, "name", "u_name");
        setField(dto, "summary", "u_summary");
        setField(dto, "content", "u_content");
        setField(dto, "thumbnail", "u_thumbnail");
        setField(dto, "icon", "u_icon");
        setField(dto, "operationInfo", operationInfoDto);
        setField(dto, "locationInfo", locationInfoDto);
        setField(dto, "buttonInfo", buttonInfoDto);
        setField(dto, "durationBinding", durationBindingDto);
        return dto;
    }

    public static MapResDto 지도_응답_DTO(Map map) {
        return MapResDto.from(map);
    }

    public static Map 기본_지도_엔티티() {
        MapCreateDto mapCreateDto = 지도_생성_DTO();
        MapCategory mapCategory = MapCategoryFixture.지도카테고리_엔티티();
        return Map.of(mapCreateDto, mapCategory);
    }

    // TODO: Duration 관련 Fixture 생성후 적용하기
    public static Map 지도_엔티티(MapCreateDto mapCreateDto, MapCategory mapCategory, long count) {
        Map map = Map.of(mapCreateDto, mapCategory);
        List<DurationMap> durationMaps = DurationMap_추가(count);
        setField(map, "durationMaps", durationMaps);
        return map;
    }

    private static List<DurationMap> DurationMap_추가(long count) {
        List<DurationMap> durationMaps = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            Duration duration = new Duration();
            setField(duration, "id", i);
            DurationMap durationMap1 = new DurationMap();
            setField(durationMap1, "id", i);
            setField(durationMap1, "duration", duration);
            durationMaps.add(durationMap1);
        }
        return durationMaps;
    }
}
