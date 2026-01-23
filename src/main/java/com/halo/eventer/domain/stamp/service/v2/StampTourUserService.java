package com.halo.eventer.domain.stamp.service.v2;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.member.dto.TokenDto;
import com.halo.eventer.domain.member.exception.LoginFailedException;
import com.halo.eventer.domain.stamp.*;
import com.halo.eventer.domain.stamp.dto.mission.response.MissionBoardResDto;
import com.halo.eventer.domain.stamp.dto.mission.response.MissionExtraInfoSummaryResDto;
import com.halo.eventer.domain.stamp.dto.mission.response.MissionTemplateResDto;
import com.halo.eventer.domain.stamp.dto.stamp.response.ButtonResDto;
import com.halo.eventer.domain.stamp.dto.stampUser.request.StampUserLoginDto;
import com.halo.eventer.domain.stamp.dto.stampUser.request.StampUserSignupReqDto;
import com.halo.eventer.domain.stamp.dto.stampUser.response.PrizeClaimQrResDto;
import com.halo.eventer.domain.stamp.exception.*;
import com.halo.eventer.domain.stamp.repository.*;
import com.halo.eventer.global.security.provider.JwtProvider;
import com.halo.eventer.global.utils.EncryptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StampTourUserService {

    private final StampUserRepository stampUserRepository;
    private final EncryptService encryptService;
    private final StampRepository stampRepository;
    private final JwtProvider jwtProvider;
    private final MissionRepository missionRepository;
    private final MissionDetailsTemplateRepository templateRepository;
    private final UserMissionRepository userMissionRepository;

    @Transactional
    public void signup(long festivalId, long stampId, StampUserSignupReqDto request) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        StampUser stampUser = createStampUserFromSignUpDto(stampId, request);
        stampUser.addStamp(stamp);
        stamp.assignAllMissionsTo(stampUser);
        stampUserRepository.save(stampUser);
    }

    @Transactional
    // TODO : 로그인 방식 수정 필요
    public TokenDto login(long festivalId, long stampId, StampUserLoginDto request) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        StampUser stampUser = loadStampUserWithStampIdAndLoginInfo(stampId, request);
        String token = jwtProvider.createToken(stampUser.getUuid(), List.of("STAMP"));
        return new TokenDto(token);
    }

    @Transactional(readOnly = true)
    public MissionBoardResDto getMissionBoard(long festivalId, long stampId, String userUuid) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        StampUser user = loadStampUserOrThrow(stampId, userUuid);
        List<UserMission> userMissions = filterUserMissionOnlyShowing(user);
        return MissionBoardResDto.from(userMissions, user.getFinished());
    }

    //    private void syncUserMissions(Stamp stamp, StampUser user) {
    //        Set<Long> assigned = user.getUserMissions().stream()
    //                .map(um -> um.getMission().getId())
    //                .collect(Collectors.toSet());
    //
    //        List<UserMission> missing = stamp.getMissions().stream()
    //                .filter(m -> !assigned.contains(m.getId()))
    //                .map(m -> UserMission.create(m, user))
    //                .toList();
    //
    //        if (!missing.isEmpty()) {
    //            user.getUserMissions().addAll(missing);
    //            // cascade = ALL 이면 user 저장만으로 전파, 아니면 repository.saveAll(missing);
    //        }
    //    }

    @Transactional(readOnly = true)
    public MissionTemplateResDto getMissionsTemplate(long festival, long stampId, long missionId, String userUuid) {
        ensureStamp(festival, stampId);
        Mission mission = loadMissionOrThrow(missionId);
        MissionDetailsTemplate template = loadMissionDetailsTemplate(missionId);
        UserMission userMission = loadUserMissionOrThrow(stampId, missionId, userUuid);
        return new MissionTemplateResDto(
                mission.getId(),
                mission.getShowTitle() ? mission.getTitle() : null,
                template.getMediaSpec(),
                template.getMediaUrl(),
                template.getDesignLayout(),
                userMission.getSuccessCount(),
                mission.getShowRequiredSuccessCount() ? mission.getRequiredSuccessCount() : null,
                MissionExtraInfoSummaryResDto.fromEntities(template.getExtraInfo()),
                template.getButtonLayout(),
                ButtonResDto.fromEntities(template.getButtons()));
    }

    @Transactional
    public void successMissionByQr(long festivalId, long stampId, long missionId, String userUuid) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        Mission mission = loadMissionOrThrow(missionId);
        StampUser stampUser = loadStampUserOrThrow(stampId, userUuid);
        UserMission um = loadUserMissionOrThrow(stampId, missionId, userUuid);
        um.increaseSuccess(missionId);
    }

    @Transactional
    public PrizeClaimQrResDto getPrizeReceiveQr(long festivalId, long stampId, String userUuid) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        StampUser stampUser = loadStampUserOrThrow(stampId, userUuid);
        return PrizeClaimQrResDto.from(
                decryptStampUserName(stampUser.getName()),
                decryptStampUserPhone(stampUser.getPhone()),
                stampUser.getParticipantCount(),
                stampUser.getExtraText());
    }

    private String decryptStampUserName(String encodedName) {
        return encryptService.decryptInfo(encodedName);
    }

    private String decryptStampUserPhone(String encodedPhone) {
        return encryptService.decryptInfo(encodedPhone);
    }

    private StampUser createStampUserFromSignUpDto(Long stampId, StampUserSignupReqDto request) {
        String encryptedPhone = encryptService.encryptInfo(request.getPhone());
        String encryptedName = encryptService.encryptInfo(request.getName());
        validateExistStampUser(stampId, encryptedPhone);
        return new StampUser(encryptedPhone, encryptedName, request.getParticipantCount(), request.getExtraText());
    }

    private StampUser loadStampUserWithStampIdAndLoginInfo(Long stampId, StampUserLoginDto loginDto) {
        return stampUserRepository
                .findByStampIdAndPhoneAndName(
                        stampId,
                        encryptService.encryptInfo(loginDto.getPhone()),
                        encryptService.encryptInfo(loginDto.getName()))
                .orElseThrow(LoginFailedException::new);
    }

    private void validateExistStampUser(Long stampId, String encryptedPhone) {
        if (stampUserRepository.existsByStampIdAndPhone(stampId, encryptedPhone)) {
            throw new StampUserAlreadyExistsException();
        }
    }

    private List<UserMission> filterUserMissionOnlyShowing(StampUser user) {
        return user.getUserMissions().stream()
                .filter(um -> um.getMission() != null && um.getMission().getShowMission())
                .sorted(Comparator.comparing(um -> um.getMission().getId()))
                .toList();
    }

    private Stamp ensureStamp(long festivalId, long stampId) {
        Stamp stamp = loadStampOrThrow(stampId);
        stamp.ensureStampInFestival(festivalId);
        return stamp;
    }

    private Stamp loadStampOrThrow(long stampId) {
        return stampRepository.findById(stampId).orElseThrow(() -> new StampNotFoundException(stampId));
    }

    private Mission loadMissionOrThrow(long missionId) {
        return missionRepository.findById(missionId).orElseThrow(() -> new MissionNotFoundException(missionId));
    }

    private StampUser loadStampUserOrThrow(long stampId, String userUuid) {
        return stampUserRepository
                .findByUuidAndStampIdWithMissions(userUuid, stampId)
                .orElseThrow(() -> new StampUserNotFoundException(userUuid));
    }

    private MissionDetailsTemplate loadMissionDetailsTemplate(long missionId) {
        return templateRepository
                .findByMissionId(missionId)
                .orElseThrow(() -> new MissionDetailsTemplateNotFoundException(missionId));
    }

    private UserMission loadUserMissionOrThrow(long stampId, long missionId, String userUuid) {
        return userMissionRepository
                .findByUserUuidAndStampIdAndMissionId(userUuid, stampId, missionId)
                .orElseThrow(() -> new IllegalStateException("USER_MISSION_NOT_FOUND"));
    }
}
