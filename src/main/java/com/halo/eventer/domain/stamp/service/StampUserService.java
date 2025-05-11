package com.halo.eventer.domain.stamp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.stamp.Custom;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.StampUser;
import com.halo.eventer.domain.stamp.dto.stampUser.*;
import com.halo.eventer.domain.stamp.exception.*;
import com.halo.eventer.domain.stamp.repository.StampRepository;
import com.halo.eventer.domain.stamp.repository.StampUserRepository;
import com.halo.eventer.global.utils.EncryptService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StampUserService {

    private final StampUserRepository stampUserRepository;
    private final EncryptService encryptService;
    private final StampRepository stampRepository;

    @Transactional
    public StampUserGetDto signup(Long stampId, SignupDto signupDto) {
        Stamp stamp = loadStampOrThrow(stampId);
        stamp.validateStampOn();
        StampUser stampUser = createStampUser(stamp, signupDto);
        stampUser.assignMissionsFrom(stamp);
        stampUserRepository.save(stampUser);
        return toStampUserGetDto(stampUser);
    }

    @Transactional
    public StampUserGetDto login(Long stampId, LoginDto loginDto) {
        StampUser stampUser = loadStampUserWithStampIdAndLoginInfo(stampId, loginDto);
        List<UserMissionInfoGetDto> userMissionInfoGetDtos =
                UserMissionInfoGetDto.fromEntities(stampUser.getUserMissions());
        return StampUserGetDto.from(stampUser, userMissionInfoGetDtos);
    }

    @Transactional(readOnly = true)
    public UserMissionInfoWithFinishedGetListDto getUserMissionWithFinished(String uuid) {
        StampUser stampUser = loadStampUserFromUuid(uuid);
        List<UserMissionInfoGetDto> userMissionInfoGetDtos =
                UserMissionInfoGetDto.fromEntities(stampUser.getUserMissions());
        return new UserMissionInfoWithFinishedGetListDto(stampUser.isFinished(), userMissionInfoGetDtos);
    }

    @Transactional
    public void updateUserMission(String uuid, Long userMissionId) {
        StampUser stampUser = loadStampUserFromUuid(uuid);
        stampUser.userMissionComplete(userMissionId);
    }

    @Transactional
    public String checkFinish(String uuid) {
        StampUser stampUser = loadStampUserFromUuid(uuid);
        if (stampUser.isAllMissionsCompleted()) {
            stampUser.finished();
            return "스탬프 투어 완료";
        }
        return "미완료";
    }

    @Transactional
    public String checkV2Finish(String uuid) {
        StampUser stampUser = loadStampUserFromUuid(uuid);
        if (!stampUser.canFinishTour()) {
            return "미완료";
        }
        stampUser.finished();
        return "스탬프 투어 완료";
    }

    @Transactional
    public void deleteStampByUuid(String uuid) {
        StampUser stampUser = loadStampUserFromUuid(uuid);
        stampUserRepository.delete(stampUser);
    }

    private Stamp loadStampOrThrow(Long stampId) {
        return stampRepository.findById(stampId).orElseThrow(() -> new StampNotFoundException(stampId));
    }

    private StampUser loadStampUserFromUuid(String uuid) {
        return stampUserRepository.findByUuid(uuid).orElseThrow(() -> new StampUserNotFoundException(uuid));
    }

    private StampUser loadStampUserWithStampIdAndLoginInfo(Long stampId, LoginDto loginDto) {
        return stampUserRepository
                .findByStampIdAndPhoneAndName(
                        stampId,
                        encryptService.encryptInfo(loginDto.getPhone()),
                        encryptService.encryptInfo(loginDto.getName()))
                .orElseThrow(StampUserNotFoundException::new);
    }

    private void validateExistStamp(Long stampId, String encryptedPhone) {
        if (stampUserRepository.existsByStampIdAndPhone(stampId, encryptedPhone)) {
            throw new StampUserAlreadyExistsException();
        }
    }

    private StampUser createStampUser(Stamp stamp, SignupDto signupDto) {
        String encryptedPhone = encryptService.encryptInfo(signupDto.getPhone());
        String encryptedName = encryptService.encryptInfo(signupDto.getName());

        validateExistStamp(stamp.getId(), encryptedPhone);

        StampUser stampUser = new StampUser(stamp, encryptedPhone, encryptedName, signupDto.getParticipantCount());

        if (signupDto instanceof SignupWithCustomDto) {
            SignupWithCustomDto customDto = (SignupWithCustomDto) signupDto;
            Custom custom = new Custom();
            custom.setSchoolNo(customDto.getSchoolNo());
            stampUser.setCustom(custom);
        }

        return stampUser;
    }

    private StampUserGetDto toStampUserGetDto(StampUser user) {
        List<UserMissionInfoGetDto> userMissionInfoGetDtos = UserMissionInfoGetDto.fromEntities(user.getUserMissions());
        return StampUserGetDto.from(user, userMissionInfoGetDtos);
    }
}
