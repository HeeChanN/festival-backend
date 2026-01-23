package com.halo.eventer.domain.stamp.dto.stamp.response;

import java.util.List;

import com.halo.eventer.domain.stamp.ParticipateGuidePage;
import com.halo.eventer.domain.stamp.dto.stamp.enums.MediaSpec;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampTourGuidePageResDto {
    private long pageId;
    private String title;
    private MediaSpec mediaSpec;
    private String mediaUrl;
    private String summary;
    private String details;
    private String additional;

    public static StampTourGuidePageResDto from(ParticipateGuidePage page) {
        return new StampTourGuidePageResDto(
                page.getId(),
                page.getTitle(),
                page.getMediaSpec(),
                page.getMediaUrl(),
                page.getSummary(),
                page.getDetails(),
                page.getAdditional());
    }

    public static List<StampTourGuidePageResDto> fromEntities(List<ParticipateGuidePage> pages) {
        return pages.stream().map(StampTourGuidePageResDto::from).toList();
    }
}
