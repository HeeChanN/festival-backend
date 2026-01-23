package com.halo.eventer.domain.stamp;

import jakarta.persistence.*;

import com.halo.eventer.domain.stamp.dto.stamp.enums.ButtonAction;
import com.halo.eventer.domain.stamp.dto.stamp.request.ButtonReqDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Button {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer sequenceIndex;

    private String content;

    private String iconImg;

    @Enumerated(EnumType.STRING)
    private ButtonAction action = ButtonAction.OPEN_URL;

    private String targetUrl; // action = OPEN_URL 일 때만 사용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_template_id")
    private PageTemplate pageTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_details_template_id")
    private MissionDetailsTemplate missionDetailsTemplate;

    private Button(int sequenceIndex, String iconImg, String content, ButtonAction action, String targetUrl) {
        this.sequenceIndex = sequenceIndex;
        this.iconImg = iconImg;
        this.content = content;
        this.action = action;
        this.targetUrl = targetUrl;
        validateInvariants();
    }

    private void attachTo(PageTemplate page) {
        this.pageTemplate = page;
        page.getButtons().add(this);
    }

    private void attachTo(MissionDetailsTemplate template) {
        this.missionDetailsTemplate = template;
        template.getButtons().add(this);
    }

    private void validateInvariants() {
        if (action == ButtonAction.OPEN_URL) {
            if (targetUrl == null || targetUrl.isBlank()) {
                throw new IllegalArgumentException("OPEN_URL 버튼은 targetUrl이 필요합니다.");
            }
        }
    }

    public static Button from(PageTemplate template, ButtonReqDto dto) {
        Button b = new Button(
                dto.getSequenceIndex(), dto.getIconImgUrl(), dto.getContent(), dto.getAction(), dto.getTargetUrl());
        b.attachTo(template);
        return b;
    }

    public static Button from(MissionDetailsTemplate template, ButtonReqDto dto) {
        Button b = new Button(
                dto.getSequenceIndex(), dto.getIconImgUrl(), dto.getContent(), dto.getAction(), dto.getTargetUrl());
        b.attachTo(template);
        return b;
    }
}
