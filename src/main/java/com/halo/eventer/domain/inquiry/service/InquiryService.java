package com.halo.eventer.domain.inquiry.service;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.inquiry.Inquiry;

import com.halo.eventer.domain.inquiry.InquiryConstants;
import com.halo.eventer.domain.inquiry.dto.*;
import com.halo.eventer.domain.inquiry.exception.InquiryNotFoundException;
import com.halo.eventer.domain.inquiry.exception.InquiryUnauthorizedAccessException;
import com.halo.eventer.domain.inquiry.repository.InquiryRepository;
import com.halo.eventer.global.security.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {
  private final InquiryRepository inquiryRepository;
  private final PasswordService passwordService;
  private final FestivalRepository festivalRepository;

  public void create(Long festivalId, InquiryCreateReqDto inquiryCreateReqDto) {
    Festival festival = festivalRepository
            .findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));
    String encodedPassword = passwordService.encode(inquiryCreateReqDto.getPassword());
    Inquiry inquiry = new Inquiry(festival,inquiryCreateReqDto,encodedPassword);
    inquiryRepository.save(inquiry);
  }

  public InquiryNoOffsetPageDto getAllInquiryForAdmin(Long festivalId, Long lastId) {
    return getInquiriesWithNoOffsetPaging(festivalId,lastId);
  }

  public InquiryResDto findInquiryForAdmin(Long id) {
    Inquiry inquiry = inquiryRepository.findById(id).orElseThrow(() -> new InquiryNotFoundException(id));
    return new InquiryResDto(inquiry);
  }

  @Transactional
  public InquiryResDto updateInquiryAnswer(Long id, InquiryAnswerReqDto dto) {
    Inquiry inquiry = inquiryRepository.findById(id).orElseThrow(() -> new InquiryNotFoundException(id));
    inquiry.registerAnswer(dto.getAnswer());
    return new InquiryResDto(inquiry);
  }

  public void delete(Long id) {
    inquiryRepository.deleteById(id);
  }

  public InquiryNoOffsetPageDto getAllInquiryForUser(Long festivalId,Long lastId) {
    InquiryNoOffsetPageDto inquiryNoOffsetPageDto = getInquiriesWithNoOffsetPaging(festivalId,lastId);

    inquiryNoOffsetPageDto.updateInquiryItemDtoWithTitleVisibility();

    return  inquiryNoOffsetPageDto;
  }

  public InquiryResDto getInquiryForUser(Long id, InquiryUserReqDto dto) {
    Inquiry inquiry = inquiryRepository.findById(id).orElseThrow(() -> new InquiryNotFoundException(id));

    validateAccess(inquiry, dto);

    return new InquiryResDto(inquiry);
  }

  public InquiryNoOffsetPageDto getInquiriesWithNoOffsetPaging(Long festivalId, Long lastId) {
    int pageSize = InquiryConstants.INQUIRY_PAGE_SIZE;
    List<Inquiry> inquiries = inquiryRepository.getPageByFestivalIdAndLastId(festivalId, lastId, pageSize + 1);

    boolean isLast = inquiries.size() <= pageSize;
    if (!isLast)
      inquiries.remove(inquiries.size() - 1);

    return new InquiryNoOffsetPageDto(inquiries, isLast);
  }

  private void validateAccess(Inquiry inquiry, InquiryUserReqDto dto) {
    if (!inquiry.isSecret()) {
      return;
    }

    boolean isPasswordCorrect = passwordService.matches(dto.getPassword(), inquiry.getPassword());

    if (!inquiry.isOwner(dto) || !isPasswordCorrect) {
      throw new InquiryUnauthorizedAccessException();
    }
  }
}
