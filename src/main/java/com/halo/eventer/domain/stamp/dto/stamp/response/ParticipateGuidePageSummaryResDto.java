package com.halo.eventer.domain.stamp.dto.stamp.response;

import java.util.List;

import com.halo.eventer.domain.stamp.ParticipateGuidePage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipateGuidePageSummaryResDto {
    private Long pageId;
    private String title;
    private int displayOrder;

    public static ParticipateGuidePageSummaryResDto from(ParticipateGuidePage page) {
        return new ParticipateGuidePageSummaryResDto(
                page.getId(), page.getTitle(), page.getDisplayOrderFeature().getDisplayOrder());
    }

    public static List<ParticipateGuidePageSummaryResDto> fromEntities(List<ParticipateGuidePage> pages) {
        return pages.stream().map(ParticipateGuidePageSummaryResDto::from).toList();
    }
}
