package com.halo.eventer.domain.inquiry.controller;



import com.halo.eventer.domain.inquiry.dto.*;
import com.halo.eventer.domain.inquiry.service.InquiryService;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class inquiryController {

    private final InquiryService inquiryService;

    @PostMapping
    public void create(@RequestParam("festivalId") Long festivalId ,
                                @RequestBody InquiryCreateReqDto inquiryCreateReqDto){
        inquiryService.create(festivalId,inquiryCreateReqDto);
    }

    @GetMapping("/forAdmin")
    public InquiryNoOffsetPageDto getInquiriesForAdmin(@RequestParam("festivalId") Long festivalId,
                                                        @RequestParam("lastId") @Min(0) Long lastId){
        return inquiryService.getAllInquiryForAdmin(festivalId,lastId);
    }

    @GetMapping("/forAdmin/{inquiryId}")
    public InquiryResDto getInquiryForAdmin(@PathVariable("inquiryId") Long id){
        return new InquiryResDto(inquiryService.findInquiryForAdmin(id));
    }

    @PatchMapping("/forAdmin")
    public InquiryResDto updateInquiryAnswer(@RequestParam("inquiryId") Long id,
                                       @RequestBody InquiryAnswerReqDto answerReqDto){
        return new InquiryResDto(inquiryService.updateInquiryAnswer(id, answerReqDto));
    }

    @DeleteMapping("/forAdmin")
    public void delete(@RequestParam("inquiryId") Long id){
        inquiryService.delete(id);
    }

    @GetMapping("/forUser")
    public InquiryNoOffsetPageDto getAllInquiresForUser(@RequestParam("festivalId") Long festivalId,
                                                   @RequestParam("lastId") @Min(0) Long lastId){
        return inquiryService.getAllInquiryForUser(festivalId,lastId);
    }

    @PostMapping("/forUser/{inquiryId}")
    public InquiryResDto getInquiryForUser(@PathVariable("inquiryId") Long id, @RequestBody InquiryUserReqDto dto){
        return inquiryService.getInquiryForUser(id,dto);
    }
}
