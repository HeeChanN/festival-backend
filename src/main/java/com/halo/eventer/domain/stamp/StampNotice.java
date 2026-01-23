package com.halo.eventer.domain.stamp;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class StampNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String cautionContent;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String personalInformationContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id")
    private Stamp stamp;

    @Builder
    private StampNotice(Stamp stamp, String cautionContent, String personalInformationContent) {
        this.stamp = stamp;
        this.cautionContent = (cautionContent == null) ? "" : cautionContent;
        this.personalInformationContent = (personalInformationContent == null) ? "" : personalInformationContent;
        stamp.getStampNotices().add(this);
    }

    public void update(String caution, String privacy) {
        String safeCaution = (caution == null) ? "" : caution;
        String safePrivacy = (privacy == null) ? "" : privacy;
        this.cautionContent = safeCaution;
        this.personalInformationContent = safePrivacy;
    }

    public static StampNotice from(Stamp stamp, String participationNotice, String privacyConsent) {
        return StampNotice.builder()
                .stamp(stamp)
                .cautionContent(participationNotice)
                .personalInformationContent(privacyConsent)
                .build();
    }

    public static StampNotice defaultNotice(Stamp stamp) {
        return StampNotice.builder()
                .stamp(stamp)
                .cautionContent("")
                .personalInformationContent("")
                .build();
    }
}
