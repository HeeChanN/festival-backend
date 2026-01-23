package com.halo.eventer.domain.stamp.service.v2;

import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.StampUser;
import com.halo.eventer.domain.stamp.UserMission;
import com.halo.eventer.domain.stamp.dto.mission.request.MissionClearReqDto;
import com.halo.eventer.domain.stamp.dto.stampUser.enums.Finished;
import com.halo.eventer.domain.stamp.dto.stampUser.enums.SortType;
import com.halo.eventer.domain.stamp.dto.stampUser.request.MissionCompletionUpdateReq;
import com.halo.eventer.domain.stamp.dto.stampUser.request.StampUserInfoUpdateReqDto;
import com.halo.eventer.domain.stamp.dto.stampUser.response.*;
import com.halo.eventer.domain.stamp.exception.StampNotFoundException;
import com.halo.eventer.domain.stamp.exception.StampUserNotFoundException;
import com.halo.eventer.domain.stamp.exception.UserMissionNotFoundException;
import com.halo.eventer.domain.stamp.repository.StampRepository;
import com.halo.eventer.domain.stamp.repository.StampUserRepository;
import com.halo.eventer.global.common.page.PageInfo;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.utils.EncryptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StampUserAdminService {

    private final StampRepository stampRepository;
    private final StampUserRepository stampUserRepository;
    private final EncryptService encryptService;

    @Transactional(readOnly = true)
    public List<StampUserInfoResDto> getAllStampUsers(long festivalId, long stampId) {
        ensureStamp(festivalId, stampId);
        List<StampUser> stampUsers = stampUserRepository.findAllByStampId(stampId);
        return decryptStampUserInfo(stampUsers);
    }

    @Transactional(readOnly = true)
    public PagedResponse<StampUserSummaryResDto> getStampUsers(
            long festivalId, long stampId, String q, Finished finished, int page, int size, SortType sortType) {
        ensureStamp(festivalId, stampId);

        // 이름 정렬 요청이면 DB 정렬을 createdAt(또는 id)로 고정
        Sort sort = (sortType == SortType.NAME)
                ? Sort.by(Sort.Direction.ASC, "createdAt")
                : Sort.by(Sort.Direction.ASC, sortType.getProperty());

        Pageable pageable = PageRequest.of(page, size, sort);
        Boolean clearedFilter = finished.toBoolean();

        Page<StampUser> result = selectQueryBySearchTerm(stampId, q, clearedFilter, pageable);
        return convertToPagedResponse(result, sortType);
    }

    @Transactional(readOnly = true)
    public StampUserDetailResDto getUserDetail(long festivalId, long stampId, long userId) {
        ensureStamp(festivalId, stampId);
        StampUser stampUser = loadStampUserFromIdAndStampId(userId, stampId);
        List<UserMissionStatusResDto> missions = stampUser.getUserMissions().stream()
                .sorted(Comparator.comparing(um -> um.getMission().getId()))
                .map(um -> new UserMissionStatusResDto(
                        um.getId(), um.getMission().getId(), um.getMission().getTitle(), um.getComplete()))
                .toList();

        return new StampUserDetailResDto(
                decryptStampUserName(stampUser.getName()),
                decryptStampUserPhone(stampUser.getPhone()),
                stampUser.getUuid(),
                stampUser.getReceivedPrizeName(),
                stampUser.getFinished(),
                missions,
                stampUser.getExtraText(),
                stampUser.getParticipantCount());
    }

    @Transactional
    public void updateStampUserInfo(long festivalId, long stampId, long userId, StampUserInfoUpdateReqDto request) {
        ensureStamp(festivalId, stampId);
        StampUser stampUser = loadStampUserFromIdAndStampId(userId, stampId);
        stampUser.updateInfo(
                encryptService.encryptInfo(request.getName()),
                encryptService.encryptInfo(request.getPhone()),
                request.getExtraText(),
                request.getParticipantCount());
    }

    @Transactional
    public void updateUserMissionState(
            long festivalId, long stampId, long userId, long userMissionId, MissionClearReqDto request) {
        ensureStamp(festivalId, stampId);
        StampUser stampUser = loadStampUserFromIdAndStampId(userId, stampId);
        UserMission target = stampUser.getUserMissions().stream()
                .filter(um -> um.getId().equals(userMissionId))
                .findFirst()
                .orElseThrow(() -> new UserMissionNotFoundException(userMissionId));
        if (request.isClear()) {
            target.markAsComplete();
            return;
        }
        target.markAsIncomplete();
    }

    @Transactional
    public void updateStampUserPrizeAndFinished(
            long festivalId, long stampId, long userId, MissionCompletionUpdateReq request) {
        ensureStamp(festivalId, stampId);
        StampUser su = loadUserWithMissionsOrThrow(stampId, userId);
        su.markAsFinished(request.isFinished());
        su.updateReceivedPrizeName(request.getReceivedPrizeName());
    }

    @Transactional(readOnly = true)
    public StampUserUserIdResDto getStampUserId(long festivalId, long stampId, String uuid) {
        ensureStamp(festivalId, stampId);
        StampUser stampUser = loadStampUserOrThrow(stampId, uuid);
        return new StampUserUserIdResDto(stampUser.getId());
    }

    private void ensureStamp(long festivalId, long stampId) {
        Stamp stamp = loadStampOrThrow(stampId);
        stamp.ensureStampInFestival(festivalId);
    }

    private Stamp loadStampOrThrow(long stampId) {
        return stampRepository.findById(stampId).orElseThrow(() -> new StampNotFoundException(stampId));
    }

    private StampUser loadStampUserOrThrow(long stampId, String userUuid) {
        return stampUserRepository
                .findByUuidAndStampIdWithMissions(userUuid, stampId)
                .orElseThrow(() -> new StampUserNotFoundException(userUuid));
    }

    private StampUser loadStampUserFromIdAndStampId(long userId, long stampId) {
        return stampUserRepository
                .findByIdAndStampIdWithMissions(userId, stampId)
                .orElseThrow(() -> new StampUserNotFoundException(userId));
    }

    private StampUser loadUserWithMissionsOrThrow(long stampId, long userId) {
        return stampUserRepository
                .findByIdAndStampIdWithMissions(userId, stampId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private List<StampUserInfoResDto> decryptStampUserInfo(List<StampUser> stampUsers) {
        return stampUsers.stream()
                .map(stampUser -> new StampUserInfoResDto(
                        stampUser.getId(),
                        decryptStampUserName(stampUser.getName()),
                        decryptStampUserPhone(stampUser.getPhone()),
                        stampUser.getUuid(),
                        stampUser.getFinished(),
                        stampUser.getParticipantCount(),
                        stampUser.getReceivedPrizeName(),
                        stampUser.getCreatedAt()))
                .toList();
    }

    private Page<StampUser> selectQueryBySearchTerm(long stampId, String q, Boolean cleared, Pageable pageable) {
        String qParam = (q == null || q.isBlank()) ? null : encryptService.encryptInfo(q.trim());
        return stampUserRepository.searchUsers(stampId, qParam, cleared, pageable);
    }

    private PagedResponse<StampUserSummaryResDto> convertToPagedResponse(Page<StampUser> result, SortType sortType) {
        // DTO 리스트 생성 (수정 가능한 리스트로)
        List<StampUserSummaryResDto> content = new java.util.ArrayList<>(result.getContent().stream()
                .map(su -> new StampUserSummaryResDto(
                        su.getId(),
                        decryptStampUserName(su.getName()),
                        decryptStampUserPhone(su.getPhone()),
                        su.getUuid(),
                        su.getFinished(),
                        su.getCreatedAt()))
                .toList());

        // 이름 정렬이면 한국어 Collator로 페이지 내 정렬
        if (sortType == SortType.NAME) {
            java.text.Collator collator = java.text.Collator.getInstance(java.util.Locale.KOREAN);
            collator.setStrength(java.text.Collator.PRIMARY);
            content.sort(java.util.Comparator.comparing(StampUserSummaryResDto::getName, collator));
        }

        PageInfo pageInfo = PageInfo.builder()
                .pageNumber(result.getNumber() + 1)
                .pageSize(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();

        return PagedResponse.<StampUserSummaryResDto>builder()
                .content(content)
                .pageInfo(pageInfo)
                .build();
    }

    private String decryptStampUserName(String encodedName) {
        return encryptService.decryptInfo(encodedName);
    }

    private String decryptStampUserPhone(String encodedPhone) {
        return encryptService.decryptInfo(encodedPhone);
    }
}
