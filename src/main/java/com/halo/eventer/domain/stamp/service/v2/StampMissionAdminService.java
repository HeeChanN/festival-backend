package com.halo.eventer.domain.stamp.service.v2;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.stamp.*;
import com.halo.eventer.domain.stamp.dto.mission.request.*;
import com.halo.eventer.domain.stamp.dto.mission.response.*;
import com.halo.eventer.domain.stamp.dto.stamp.request.PrizeExchangeImgReqDto;
import com.halo.eventer.domain.stamp.exception.*;
import com.halo.eventer.domain.stamp.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StampMissionAdminService {

    private final StampRepository stampRepository;
    private final StampMissionPrizeRepository prizeRepository;
    private final MissionRepository missionRepository;
    private final MissionDetailsTemplateRepository templateRepository;

    @Transactional
    public void createMission(long festivalId, long stampId, String missionName, Boolean showMission) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        Mission mission = Mission.from(stamp, missionName, showMission);
        missionRepository.save(mission);
        stamp.updateMissionCount();
    }

    @Transactional(readOnly = true)
    public MissionListResDto getMissions(long festivalId, long stampId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        List<Mission> missions = loadAllMissions(stampId);
        List<MissionBriefResDto> missionList = MissionBriefResDto.fromEntities(missions);
        return new MissionListResDto(missionList);
    }

    @Transactional
    public void deleteMission(long festivalId, long stampId, long missionId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        Mission mission = loadMissionOrThrow(stampId, missionId);
        stamp.getMissions().remove(mission);
        missionRepository.delete(mission);
        stamp.updateMissionCount();
    }

    @Transactional
    public void showMissionsTrue(long festivalId, long stampId, MissionsAllShowReqDto request) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        List<Mission> missions = loadAllMissions(stampId);
        missions.forEach(mission -> mission.updateMissionShow(request.getShowMissions()));
    }

    @Transactional
    public void toggleMissionShowing(long festivalId, long stampId, long missionId, MissionShowReqDto request) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        Mission mission = loadMissionOrThrow(stampId, missionId);
        mission.updateMissionShow(request.isShowMission());
    }

    @Transactional(readOnly = true)
    public MissionShowResDto showMission(long festivalId, long stampId, long missionId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        Mission mission = loadMissionOrThrow(stampId, missionId);
        return MissionShowResDto.from(mission);
    }

    @Transactional
    public StampMissionBasicSettingsResDto getBasicSettings(long festivalId, long stampId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        return StampMissionBasicSettingsResDto.from(stamp);
    }

    @Transactional
    public void updateBasicSettings(long festivalId, long stampId, final MissionBasicSettingsReqDto request) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        stamp.updateBasic(request.getMissionCount(), request.getMissionDetailsDesignLayout());
    }

    @Transactional
    public List<MissionPrizeResDto> getPrizes(long festivalId, long stampId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        List<StampMissionPrize> prizes = stamp.getPrizes();
        return MissionPrizeResDto.fromEntities(prizes);
    }

    @Transactional
    public void addPrize(long festivalId, long stampId, final MissionPrizeCreateReqDto request) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        StampMissionPrize prize =
                StampMissionPrize.from(request.getRequiredCount(), request.getPrizeDescription(), stamp);
        prizeRepository.save(prize);
    }

    @Transactional
    public void updatePrize(long festivalId, long stampId, long prizeId, final MissionPrizeUpdateReqDto request) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        StampMissionPrize prize = loadStampMissionPrizeOrThrow(prizeId, stampId);
        prize.update(request.getRequiredCount(), request.getPrizeDescription());
    }

    @Transactional
    public void updatePrizeTicketImg(long festivalId, long stampId, final PrizeExchangeImgReqDto request) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        stamp.updatePrizeTicketImg(request.getPrizeExchangeImgType(), request.getCustomExchangeImgUrl());
    }

    @Transactional
    public void deletePrize(long festivalId, long stampId, long prizeId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        StampMissionPrize prize = loadStampMissionPrizeOrThrow(prizeId, stampId);
        Stamp parent = prize.getStamp();
        if (parent != null) {
            parent.getPrizes().remove(prize);
        }
        prizeRepository.delete(prize);
    }

    @Transactional(readOnly = true)
    public StampMissionClearImageResDto getStampMissionCompleteImage(long festivalId, long stampId, long missionId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        Mission mission = loadMissionOrThrow(stampId, missionId);
        return StampMissionClearImageResDto.from(
                mission.getShowTitle(), mission.getClearedThumbnail(), mission.getNotClearedThumbnail());
    }

    @Transactional
    public void updateStampMissionCompleteImage(
            long festivalId, long stampId, long missionId, final StampMissionClearImageReqDto request) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        Mission mission = loadMissionOrThrow(stampId, missionId);
        mission.updateTitleVisible(request.isShowMissionTitle());
        mission.updateClearedImage(request.getClearedThumbnail(), request.getNotClearedThumbnail());
    }

    @Transactional
    public MissionDetailsTemplateResDto getMissionDetailsTemplate(long festivalId, long stampId, long missionId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        Mission mission = loadMissionOrThrow(stampId, missionId);
        MissionDetailsTemplate template = loadOrCreateMissionDetailsTemplate(mission);
        return MissionDetailsTemplateResDto.from(
                template, mission.getShowTitle(), mission.getShowRequiredSuccessCount(), mission.getTitle());
    }

    @Transactional
    public void upsertMissionDetailsTemplate(
            long festivalId, long stampId, long missionId, final MissionDetailsTemplateReqDto request) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        Mission mission = loadMissionOrThrow(stampId, missionId);
        MissionDetailsTemplate template = loadMissionDetailsTemplate(mission);
        mission.updateTitle(request.getMissionTitle());
        mission.updateRequiredSuccessCountVisible(request.isShowSuccessCount());
        template.update(
                request.getMissionDetailsDesignLayout(),
                request.isShowExtraInfos(),
                request.isShowButtons(),
                request.getExtraInfoType(),
                request.getMediaSpec(),
                request.getMediaUrl(),
                request.getButtonLayout());
        template.replaceExtraInfos(request.getExtraInfos());
        template.updateButtons(request.getButtons());
    }

    @Transactional(readOnly = true)
    public List<MissionQrDataResDto> getMissionsQrData(long festivalId, long stampId) {
        ensureStamp(festivalId, stampId);
        List<Mission> missions = loadAllMissions(stampId);
        return MissionQrDataResDto.fromEntities(missions);
    }

    private Stamp ensureStamp(long festivalId, long stampId) {
        Stamp stamp = loadStampOrThrow(stampId);
        stamp.ensureStampInFestival(festivalId);
        return stamp;
    }

    private MissionDetailsTemplate loadOrCreateMissionDetailsTemplate(Mission mission) {
        return templateRepository
                .findByMissionId(mission.getId())
                .orElseGet(() -> MissionDetailsTemplate.defaultTemplate(mission));
    }

    private MissionDetailsTemplate loadMissionDetailsTemplate(Mission mission) {
        return templateRepository
                .findByMissionId(mission.getId())
                .orElseThrow(() -> new MissionDetailsTemplateNotFoundException(mission.getId()));
    }

    private Mission loadMissionOrThrow(long stampId, long missionId) {

        return missionRepository
                .findByIdAndStampId(missionId, stampId)
                .orElseThrow(() -> new MissionNotFoundException(missionId));
    }

    private StampMissionPrize loadStampMissionPrizeOrThrow(long prizeId, long stampId) {
        return prizeRepository
                .findByIdAndStampId(prizeId, stampId)
                .orElseThrow(() -> new MissionPrizeNotFoundException(prizeId));
    }

    private Stamp loadStampOrThrow(long id) {
        return stampRepository.findById(id).orElseThrow(() -> new StampNotFoundException(id));
    }

    private List<Mission> loadAllMissions(long stampId) {
        return missionRepository.findAllByStampId(stampId);
    }
}
