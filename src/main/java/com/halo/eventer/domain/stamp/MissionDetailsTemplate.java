package com.halo.eventer.domain.stamp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import jakarta.persistence.*;

import com.halo.eventer.domain.stamp.dto.mission.request.MissionDetailsTemplateReqDto;
import com.halo.eventer.domain.stamp.dto.mission.request.MissionExtraInfoUpdateReqDto;
import com.halo.eventer.domain.stamp.dto.stamp.enums.ButtonLayout;
import com.halo.eventer.domain.stamp.dto.stamp.enums.ExtraInfoLayout;
import com.halo.eventer.domain.stamp.dto.stamp.enums.MediaSpec;
import com.halo.eventer.domain.stamp.dto.stamp.enums.MissionDetailsDesignLayout;
import com.halo.eventer.domain.stamp.dto.stamp.request.ButtonReqDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MissionDetailsTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MissionDetailsDesignLayout designLayout;

    private Boolean showExtraInfos = true;
    private Boolean showButtons = true;

    @Enumerated(EnumType.STRING)
    private MediaSpec mediaSpec = MediaSpec.NONE;

    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    private ExtraInfoLayout infoLayout = ExtraInfoLayout.LIST;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MissionExtraInfo> extraInfo = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ButtonLayout buttonLayout = ButtonLayout.NONE;

    @OneToMany(mappedBy = "missionDetailsTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Button> buttons = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @Builder
    private MissionDetailsTemplate(
            MissionDetailsDesignLayout layout,
            boolean showExtraInfos,
            boolean showButtons,
            MediaSpec mediaSpec,
            String mediaUrl,
            ExtraInfoLayout type) {
        this.designLayout = layout;
        this.showExtraInfos = showExtraInfos;
        this.showButtons = showButtons;
        this.mediaSpec = mediaSpec;
        this.mediaUrl = mediaUrl;
        this.infoLayout = type;
    }

    public void update(
            MissionDetailsDesignLayout layout,
            boolean showExtraInfos,
            boolean showButtons,
            ExtraInfoLayout infoLayout,
            MediaSpec mediaSpec,
            String mediaUrl,
            ButtonLayout buttonLayout) {
        this.designLayout = layout;
        this.showExtraInfos = showExtraInfos;
        this.infoLayout = infoLayout;
        this.showButtons = showButtons;
        this.mediaSpec = mediaSpec;
        this.mediaUrl = mediaUrl;
        this.buttonLayout = buttonLayout;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
        mission.getMissionDetailsTemplates().add(this);
    }

    public void replaceExtraInfos(List<MissionExtraInfoUpdateReqDto> reqs) {
        List<MissionExtraInfoUpdateReqDto> safe = (reqs == null) ? List.of() : reqs;
        this.extraInfo.clear();
        for (MissionExtraInfoUpdateReqDto missionExtraInfoUpdateReqDto : safe) {
            MissionExtraInfo info = MissionExtraInfo.from(missionExtraInfoUpdateReqDto);
            info.setMissionDetailsTemplate(this);
        }
    }

    public void updateButtons(List<ButtonReqDto> requestButtons) {
        List<ButtonReqDto> safe = (requestButtons == null) ? List.of() : requestButtons;
        ensureUniqueSequence(safe);
        ensureLayoutCompatible(this.buttonLayout, safe.size());
        this.buttons.clear();
        safe.stream()
                .sorted(Comparator.comparingInt(ButtonReqDto::getSequenceIndex))
                .forEach(dto -> Button.from(this, dto));
    }

    private void ensureUniqueSequence(List<ButtonReqDto> list) {
        var set = new HashSet<Integer>();
        for (var b : list) {
            if (!set.add(b.getSequenceIndex())) {
                throw new IllegalArgumentException("중복된 sequenceIndex: " + b.getSequenceIndex());
            }
        }
    }

    private void ensureLayoutCompatible(ButtonLayout layout, int size) {
        if (layout == null) {
            return;
        }
        switch (layout) {
            case ONE -> {
                if (size != 1) throw new IllegalArgumentException("ButtonLayout.ONE 은 버튼 1개만 허용합니다");
            }
            case TWO_ASYM, TWO_SYM -> {
                if (size != 2) throw new IllegalArgumentException(layout + " 은 버튼 2개만 허용합니다");
            }
            case NONE -> {
                if (size != 0) throw new IllegalArgumentException("ButtonLayout.NONE 은 버튼을 허용하지 않습니다");
            }
        }
    }

    public static MissionDetailsTemplate defaultTemplate(Mission mission) {
        MissionDetailsTemplate template = new MissionDetailsTemplate();
        template.setMission(mission);
        return template;
    }

    public static MissionDetailsTemplate from(MissionDetailsTemplateReqDto request) {
        return MissionDetailsTemplate.builder()
                .layout(request.getMissionDetailsDesignLayout())
                .showExtraInfos(request.isShowExtraInfos())
                .showButtons(request.isShowButtons())
                .mediaSpec(request.getMediaSpec())
                .mediaUrl(request.getMediaUrl())
                .type(request.getExtraInfoType())
                .build();
    }
}
