package com.halo.eventer.domain.stamp;

import jakarta.persistence.*;

import com.halo.eventer.domain.stamp.dto.stamp.enums.MediaSpec;
import com.halo.eventer.domain.stamp.dto.stamp.request.StampTourParticipateGuidePageReqDto;
import com.halo.eventer.domain.widget.entity.DisplayOrderUpdatable;
import com.halo.eventer.domain.widget.feature.DisplayOrderFeature;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.halo.eventer.global.constants.DisplayOrderConstants.DISPLAY_ORDER_DEFAULT;

@Entity
@Getter
@NoArgsConstructor
public class ParticipateGuidePage implements DisplayOrderUpdatable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private MediaSpec mediaSpec = MediaSpec.NONE;

    private String mediaUrl;
    private String summary;
    private String details;
    private String additional;

    @Embedded
    @AttributeOverride(name = "displayOrder", column = @Column(name = "display_order"))
    private DisplayOrderFeature displayOrderFeature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participation_guide_id")
    private ParticipateGuide participateGuide;

    @Builder
    private ParticipateGuidePage(
            String title,
            MediaSpec mediaSpec,
            String mediaUrl,
            String summary,
            String details,
            String additional,
            ParticipateGuide participateGuide) {
        this.title = title;
        this.mediaSpec = mediaSpec;
        this.mediaUrl = mediaUrl;
        this.summary = summary;
        this.details = details;
        this.additional = additional;
        this.displayOrderFeature = DisplayOrderFeature.of(DISPLAY_ORDER_DEFAULT);
        registerParticipateGuidePage(participateGuide);
    }

    public void update(StampTourParticipateGuidePageReqDto request) {
        this.title = request.getTitle();
        this.mediaSpec = request.getMediaSpec();
        this.mediaUrl = request.getMediaUrl();
        this.summary = request.getSummary();
        this.details = request.getDetails();
        this.additional = request.getAdditional();
    }

    private void registerParticipateGuidePage(ParticipateGuide participateGuide) {
        this.participateGuide = participateGuide;
        participateGuide.getParticipateGuidePages().add(this);
    }

    public void updateDisplayOrder(Integer displayOrder) {
        this.displayOrderFeature = DisplayOrderFeature.of(displayOrder);
    }

    public static ParticipateGuidePage from(
            String title,
            MediaSpec mediaSpec,
            String mediaUrl,
            String summary,
            String details,
            String additional,
            ParticipateGuide participateGuide) {
        return new ParticipateGuidePage(title, mediaSpec, mediaUrl, summary, details, additional, participateGuide);
    }
}
