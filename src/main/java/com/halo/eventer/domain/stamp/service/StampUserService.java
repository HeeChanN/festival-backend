package com.halo.eventer.domain.stamp.service;

import com.halo.eventer.domain.stamp.Custom;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.StampUser;
import com.halo.eventer.domain.stamp.UserMission;
import com.halo.eventer.domain.stamp.dto.stampUser.*;
import com.halo.eventer.domain.stamp.exception.*;
import com.halo.eventer.domain.stamp.repository.StampRepository;
import com.halo.eventer.domain.stamp.repository.StampUserRepository;

import java.util.List;
import java.util.stream.Collectors;

import com.halo.eventer.global.utils.EncryptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StampUserService {
    private final StampUserRepository stampUserRepository;
    private final EncryptService encryptService;
    private final StampRepository stampRepository;

    private StampUser getStampUserFromUuid(String uuid) {
        return stampUserRepository.findByUuid(uuid).orElseThrow(() -> new StampUserNotFoundException(uuid));
    }

    /** 스탬프 유저 생성 */
    @Transactional
    public StampUserGetDto signup(Long stampId, SignupDto signupDto) {
        Stamp stamp = loadStampOrThrow(stampId);
        if (!stamp.isStampOn()) throw new StampClosedException(stampId);

        String encryptedPhone = encryptService.encryptInfo(signupDto.getPhone());
        String encryptedName = encryptService.encryptInfo(signupDto.getName());
        if (stampUserRepository.existsByStampIdAndPhone(stampId, encryptedPhone))
            throw new StampUserAlreadyExistsException();

        // 사용자 생성
        StampUser stampUser = new StampUser(loadStampOrThrow(stampId), encryptedPhone, encryptedName, signupDto.getParticipantCount());

        // 미션 생성 후 연결
        List<UserMission> userMissions = stamp.getMissions().stream()
                        .map(mission -> {
                            UserMission userMission = new UserMission();
                            userMission.setMission(mission);
                            userMission.setStampUser(stampUser);
                            return userMission;
                        })
                .collect(Collectors.toList());
        stampUser.setUserMission(userMissions);

        if (signupDto instanceof SignupWithCustomDto) {
            SignupWithCustomDto customDto = (SignupWithCustomDto) signupDto;
            Custom custom = new Custom();
            custom.setSchoolNo(customDto.getSchoolNo());
            stampUser.setCustom(custom);
        }

        stampUserRepository.save(stampUser);

        List<UserMissionInfoGetDto> userMissionInfoGetDtos = userMissions.stream()
                .map(userMission -> new UserMissionInfoGetDto(userMission.getId(), userMission.getMission().getId(), userMission.isComplete())).collect(Collectors.toList());

        StampUserGetDto stampUserGetDto = new StampUserGetDto(stampUser, new UserMissionInfoGetListDto(userMissionInfoGetDtos));
        return stampUserGetDto;
    }

    /** 로그인 */
    public StampUserGetDto login(Long stampId, LoginDto loginDto) {
    StampUser stampUser =
        stampUserRepository
            .findByStampIdAndPhoneAndName(
                stampId,
                encryptService.encryptInfo(loginDto.getPhone()),
                encryptService.encryptInfo(loginDto.getName()))
            .orElseThrow(StampUserNotFoundException::new);

        return new StampUserGetDto(stampUser, getUserMission(stampUser.getUuid()));
    }

    /** 유저 미션 전체 조회 */
    private UserMissionInfoGetListDto getUserMission(String uuid) {
        StampUser stampUser = getStampUserFromUuid(uuid);
        List<UserMissionInfoGetDto> userMissionInfoGetDtos = stampUser.getUserMissions().stream()
                .map(userMission -> new UserMissionInfoGetDto(userMission.getId(), userMission.getMission().getId(), userMission.isComplete()))
                .collect(Collectors.toList());

        return new UserMissionInfoGetListDto(userMissionInfoGetDtos);
    }

    /** 유저 미션 전체 조회 + finished */
    public UserMissionInfoWithFinishedGetListDto getUserMissionWithFinished(String uuid) {
        StampUser stampUser = getStampUserFromUuid(uuid);
        UserMissionInfoGetListDto userMissions = getUserMission(uuid);
        return new UserMissionInfoWithFinishedGetListDto(stampUser.isFinished(), userMissions);
    }

    /** 사용자 미션 상태 업데이트 */
    @Transactional
    public String updateUserMission(String uuid, Long userMissionId) {
        StampUser stampUser = getStampUserFromUuid(uuid);

    UserMission userMission =
        stampUser.getUserMissions().stream()
            .filter(mission -> mission.getId().equals(userMissionId))
            .findFirst()
            .orElseThrow(() -> new UserMissionNotFoundException(userMissionId));

        userMission.setComplete(true);
        stampUserRepository.save(stampUser);
        return "업데이트 성공";
    }

    /** 사용자 미션 완료 상태 확인 */
    @Transactional
    public String checkFinish(String uuid) {
        StampUser stampUser = getStampUserFromUuid(uuid);
        if (stampUser.getUserMissions().stream()
                .allMatch(UserMission::isComplete)) {
            stampUser.setFinished();
            stampUserRepository.save(stampUser);
            return "스탬프 투어 완료";
        }
        else return "미완료";
    }

    @Transactional
    public String checkV2Finish(String uuid){
        StampUser stampUser = getStampUserFromUuid(uuid);
        long count = stampUser.getUserMissions().stream()
                .filter(UserMission::isComplete)
                .count();
        if(count < stampUser.getStamp().getStampFinishCnt()){
            return "미완료";
        }
        stampUser.setFinished();
        return "스탬프 투어 완료";
    }

    /** 사용자 삭제 */
    @Transactional
    public String deleteStampByUuid(String uuid) {
        StampUser stampUser = getStampUserFromUuid(uuid);
        stampUserRepository.delete(stampUser);
        return "삭제 완료";
    }

    private Stamp loadStampOrThrow(Long stampId) {
        return stampRepository.findById(stampId)
                .orElseThrow(() -> new StampNotFoundException(stampId));
    }
}
