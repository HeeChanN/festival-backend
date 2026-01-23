package com.halo.eventer.domain.stamp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import jakarta.persistence.*;

import com.halo.eventer.domain.stamp.dto.stamp.enums.ButtonLayout;
import com.halo.eventer.domain.stamp.dto.stamp.enums.LandingPageDesignTemplate;
import com.halo.eventer.domain.stamp.dto.stamp.enums.MainPageDesignTemplate;
import com.halo.eventer.domain.stamp.dto.stamp.enums.PageType;
import com.halo.eventer.domain.stamp.dto.stamp.request.ButtonReqDto;
import com.halo.eventer.domain.stamp.dto.stamp.request.StampTourLandingPageReqDto;
import com.halo.eventer.domain.stamp.dto.stamp.request.StampTourMainPageReqDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PageTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PageType type;

    @Enumerated(EnumType.STRING)
    private MainPageDesignTemplate mainPageDesignTemplate;

    @Enumerated(EnumType.STRING)
    private LandingPageDesignTemplate landingPageDesignTemplate;

    @Column(nullable = true)
    private String backgroundImg;

    @Column(nullable = true)
    private String iconImg;

    private String description;

    @Enumerated(EnumType.STRING)
    private ButtonLayout buttonLayout = ButtonLayout.ONE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id")
    private Stamp stamp;

    @OneToMany(mappedBy = "pageTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sequenceIndex ASC")
    private List<Button> buttons = new ArrayList<>();

    private PageTemplate(Stamp stamp, PageType type) {
        registerStamp(stamp);
        this.type = type;
    }

    private void registerStamp(Stamp stamp) {
        this.stamp = stamp;
        stamp.getTemplates().add(this);
    }

    public void updateLandingPageTemplate(StampTourLandingPageReqDto request) {
        this.landingPageDesignTemplate = request.getLandingPageDesignTemplate();
        this.backgroundImg = request.getBackgroundImgUrl();
        this.iconImg = request.getIconImgUrl();
        this.description = request.getDescription();
        this.buttonLayout = request.getButtonLayout();
        if (!request.getButtonLayout().equals(ButtonLayout.NONE)) {
            updateButtons(request.getButtons());
        }
    }

    public void updateMainPageTemplate(StampTourMainPageReqDto request) {
        this.mainPageDesignTemplate = request.getMainPageDesignTemplate();
        this.backgroundImg = request.getBackgroundImgUrl();
        this.buttonLayout = request.getButtonLayout();
        if (!request.getButtonLayout().equals(ButtonLayout.NONE)) {
            updateButtons(request.getButtons());
        }
    }

    public void updateButtons(List<ButtonReqDto> requestButtons) {
        List<ButtonReqDto> safe = requestButtons == null ? List.of() : requestButtons;
        ensureUniqueSequence(safe);
        ensureLayoutCompatible(buttonLayout, safe.size());
        buttons.clear();
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

    public static PageTemplate defaultLandingPage(Stamp stamp) {
        PageTemplate pt = new PageTemplate(stamp, PageType.LANDING);
        pt.landingPageDesignTemplate = LandingPageDesignTemplate.NONE;
        return pt;
    }

    public static PageTemplate defaultMainPage(Stamp stamp) {
        PageTemplate pt = new PageTemplate(stamp, PageType.MAIN);
        pt.mainPageDesignTemplate = MainPageDesignTemplate.GRID_Nx2;
        return pt;
    }
}
