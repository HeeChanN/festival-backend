package com.halo.eventer.domain.stamp;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

import com.halo.eventer.domain.stamp.dto.stamp.enums.GuideDesignTemplate;
import com.halo.eventer.domain.stamp.dto.stamp.enums.GuideSlideMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ParticipateGuide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GuideDesignTemplate guideDesignTemplate = GuideDesignTemplate.FULL;

    @Enumerated(EnumType.STRING)
    private GuideSlideMethod guideSlideMethod = GuideSlideMethod.SLIDE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id")
    private Stamp stamp;

    @OneToMany(mappedBy = "participateGuide", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParticipateGuidePage> participateGuidePages = new ArrayList<>();

    private ParticipateGuide(Stamp stamp) {
        registerStamp(stamp);
    }

    public void update(GuideDesignTemplate guideDesignTemplate, GuideSlideMethod guideSlideMethod) {
        this.guideDesignTemplate = guideDesignTemplate;
        this.guideSlideMethod = guideSlideMethod;
    }

    private void registerStamp(Stamp stamp) {
        this.stamp = stamp;
        stamp.getParticipationGuides().add(this);
    }

    public static ParticipateGuide defaultParticipateGuide(Stamp stamp) {
        return new ParticipateGuide(stamp);
    }
}
