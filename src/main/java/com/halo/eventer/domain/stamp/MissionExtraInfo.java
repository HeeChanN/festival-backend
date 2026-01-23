package com.halo.eventer.domain.stamp;

import jakarta.persistence.*;

import com.halo.eventer.domain.stamp.dto.mission.request.MissionExtraInfoUpdateReqDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MissionExtraInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titleText; // ITEM_DESC 에서 '항목' 용

    @Column(length = 2000)
    private String bodyText; // 상세 내용 혹은 RICH_TEXT 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_details_template_id", nullable = false)
    private MissionDetailsTemplate template;

    @Builder
    private MissionExtraInfo(String titleText, String bodyText) {
        this.titleText = titleText;
        this.bodyText = bodyText;
    }

    public void setMissionDetailsTemplate(MissionDetailsTemplate template) {
        this.template = template;
        template.getExtraInfo().add(this);
    }

    public static MissionExtraInfo from(MissionExtraInfoUpdateReqDto request) {
        return MissionExtraInfo.builder()
                .titleText(request.getTitleText())
                .bodyText(request.getBodyText())
                .build();
    }
}
