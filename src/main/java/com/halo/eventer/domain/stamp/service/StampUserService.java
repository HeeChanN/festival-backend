package com.halo.eventer.domain.stamp.service;

import com.halo.eventer.domain.stamp.Custom;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.StampUser;
import com.halo.eventer.domain.stamp.UserMission;
import com.halo.eventer.domain.stamp.dto.stampUser.*;
import com.halo.eventer.domain.stamp.repository.StampUserRepository;
import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StampUserService {
    private final StampUserRepository stampUserRepository;
    private final EncryptService encryptService;
    private final StampService stampService;

    private StampUser getStampUserFromUuid(String uuid) {
        StampUser stampUser = stampUserRepository.findByUuid(uuid).orElseThrow(() -> new BaseException(ErrorCode.ELEMENT_NOT_FOUND));
        return stampUser;
    }

    /** 스탬프 유저 생성 */
    @Transactional
    public StampUserGetDto signup(Long stampId, SignupDto signupDto) {
        Stamp stamp = stampService.getStamp(stampId);
        if (!stamp.isStampOn()) throw new BaseException("종료된 스탬프 투어입니다.", ErrorCode.ELEMENT_NOT_FOUND);

        String encryptedPhone = encryptService.encryptInfo(signupDto.getPhone());
        String encryptedName = encryptService.encryptInfo(signupDto.getName());
        if (stampUserRepository.existsByStampIdAndPhone(stampId, encryptedPhone)) throw new BaseException(ErrorCode.ELEMENT_DUPLICATED);

        // 사용자 생성
        StampUser stampUser = new StampUser(stampService.getStamp(stampId), encryptedPhone, encryptedName, signupDto.getParticipantCount());

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
        StampUser stampUser = stampUserRepository.findByStampIdAndPhoneAndName(stampId, encryptService.encryptInfo(loginDto.getPhone()), encryptService.encryptInfo(loginDto.getName()))
                .orElseThrow(() -> new BaseException(ErrorCode.ELEMENT_NOT_FOUND));

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

        UserMission userMission = stampUser.getUserMissions().stream()
                        .filter(mission -> mission.getId().equals(userMissionId))
                        .findFirst()
                        .orElseThrow(() -> new BaseException(ErrorCode.ELEMENT_NOT_FOUND));

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
}
