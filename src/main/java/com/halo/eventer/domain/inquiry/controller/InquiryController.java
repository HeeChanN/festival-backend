package com.halo.eventer.domain.inquiry.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.inquiry.dto.*;
import com.halo.eventer.domain.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping
    public void create(
            @Min(1) @RequestParam("festivalId") Long festivalId,
            @Valid @RequestBody InquiryCreateReqDto inquiryCreateReqDto) {
        inquiryService.create(festivalId, inquiryCreateReqDto);
    }

    @GetMapping("/forAdmin")
    public InquiryNoOffsetPageDto getInquiriesForAdmin(
            @Min(1) @RequestParam("festivalId") Long festivalId, @Min(0) @RequestParam("lastId") Long lastId) {
        return inquiryService.getAllInquiryForAdmin(festivalId, lastId);
    }

    @GetMapping("/forAdmin/{inquiryId}")
    public InquiryResDto getInquiryForAdmin(@Min(1) @PathVariable("inquiryId") Long id) {
        return inquiryService.findInquiryForAdmin(id);
    }

    @PatchMapping("/forAdmin")
    public InquiryResDto updateInquiryAnswer(
            @Min(1) @RequestParam("inquiryId") Long inquiryId, @Valid @RequestBody InquiryAnswerReqDto answerReqDto) {
        return inquiryService.updateInquiryAnswer(inquiryId, answerReqDto);
    }

    @DeleteMapping("/forAdmin")
    public void delete(@Min(1) @RequestParam("inquiryId") Long id) {
        inquiryService.delete(id);
    }

    @GetMapping("/forUser")
    public InquiryNoOffsetPageDto getAllInquiresForUser(
            @Min(1) @RequestParam("festivalId") Long festivalId, @Min(0) @RequestParam("lastId") Long lastId) {
        return inquiryService.getAllInquiryForUser(festivalId, lastId);
    }

    @PostMapping("/forUser/{inquiryId}")
    public InquiryResDto getInquiryForUser(
            @Min(1) @PathVariable("inquiryId") Long id, @Valid @RequestBody InquiryUserReqDto dto) {
        return inquiryService.getInquiryForUser(id, dto);
    }
}
