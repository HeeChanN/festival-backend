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
public class ParticipateGuidePageDetailsResDto {
    private Long pageId;
    private String title;
    private MediaSpec mediaSpec;
    private String mediaUrl;
    private String summary;
    private String details;
    private String additional;

    public static ParticipateGuidePageDetailsResDto from(ParticipateGuidePage page) {
        return new ParticipateGuidePageDetailsResDto(
                page.getId(),
                page.getTitle(),
                page.getMediaSpec(),
                page.getMediaUrl(),
                page.getSummary(),
                page.getDetails(),
                page.getAdditional());
    }

    public static List<ParticipateGuidePageDetailsResDto> fromEntities(List<ParticipateGuidePage> pages) {
        return pages.stream().map(ParticipateGuidePageDetailsResDto::from).toList();
    }
}
