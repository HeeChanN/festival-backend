// package com.halo.eventer.domain.stamp.v2.service;
//
// import java.util.*;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
//
// import com.halo.eventer.domain.festival.Festival;
// import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
// import com.halo.eventer.domain.festival.repository.FestivalRepository;
// import com.halo.eventer.domain.stamp.*;
// import com.halo.eventer.domain.stamp.dto.stamp.enums.*;
// import com.halo.eventer.domain.stamp.dto.stamp.request.StampTourBasicUpdateReqDto;
// import com.halo.eventer.domain.stamp.dto.stamp.request.StampTourParticipateGuidePageReqDto;
// import com.halo.eventer.domain.stamp.dto.stamp.request.StampTourParticipateGuideReqDto;
// import com.halo.eventer.domain.stamp.dto.stamp.response.ParticipateGuidePageDetailsResDto;
// import com.halo.eventer.domain.stamp.dto.stamp.response.ParticipateGuidePageSummaryResDto;
// import com.halo.eventer.domain.stamp.exception.ParticipateGuideNotFoundException;
// import com.halo.eventer.domain.stamp.exception.ParticipateGuidePageNotFoundException;
// import com.halo.eventer.domain.stamp.exception.StampNotFoundException;
// import com.halo.eventer.domain.stamp.repository.*;
// import com.halo.eventer.domain.stamp.service.v2.StampTourAdminService;
// import com.halo.eventer.global.common.dto.OrderUpdateRequest;
// import com.halo.eventer.global.error.exception.BaseException;
//
// import static com.halo.eventer.domain.festival.FestivalFixture.축제_엔티티;
// import static com.halo.eventer.domain.stamp.dto.stamp.enums.ButtonLayout.TWO_SYM;
// import static com.halo.eventer.domain.stamp.v2.fixture.ButtonFixture.*;
// import static com.halo.eventer.domain.stamp.v2.fixture.PageTemplateFixture.*;
// import static com.halo.eventer.domain.stamp.v2.fixture.ParticipateGuideFixture.참여가이드_기본값;
// import static com.halo.eventer.domain.stamp.v2.fixture.ParticipateGuidePageFixture.*;
// import static com.halo.eventer.domain.stamp.v2.fixture.StampTourFixture.*;
// import static org.assertj.core.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.BDDMockito.*;
// import static org.springframework.test.util.ReflectionTestUtils.*;
//
// @ExtendWith(MockitoExtension.class)
// @SuppressWarnings("NonAsciiCharacters")
// public class StampTourAdminServiceTest {
//
//    @Mock
//    private FestivalRepository festivalRepository;
//
//    @Mock
//    private StampRepository stampRepository;
//
//    @Mock
//    private StampNoticeRepository stampNoticeRepository;
//
//    @Mock
//    private PageTemplateRepository pageTemplateRepository;
//
//    @Mock
//    private ParticipateGuideRepository participateGuideRepository;
//
//    @Mock
//    private ParticipateGuidePageRepository participateGuidePageRepository;
//
//    @InjectMocks
//    private StampTourAdminService service;
//
//    private Long 축제_id;
//    private Festival 축제;
//    private Festival 다른축제;
//
//    private Stamp 스탬프;
//    private Stamp 다른축제_스탬프;
//
//    @BeforeEach
//    void setUp() {
//        축제_id = 1L;
//        축제 = 축제_엔티티();
//        다른축제 = 축제_엔티티();
//
//        setField(축제, "id", 축제_id);
//        setField(다른축제, "id", 2L);
//
//        스탬프 = 스탬프투어1_생성(축제);
//        다른축제_스탬프 = 스탬프투어1_생성(다른축제);
//        setField(스탬프, "id", 1L);
//        setField(다른축제_스탬프, "id", 2L);
//    }
//
//    //    @Test
//    //    void 축제에_스탬프투어_생성_성공() {
//    //        String 새로운_스탬프투어_제목 = "새 스탬프 제목";
//    //        given(festivalRepository.findById(anyLong())).willReturn(Optional.of(축제));
//    //        given(stampRepository.save(any(Stamp.class))).willAnswer(inv -> inv.getArgument(0));
//    //
//    //        service.createStampTourByFestival(축제_id, 새로운_스탬프투어_제목);
//    //
//    //        then(festivalRepository).should().findById(축제_id);
//    //        then(stampRepository).should().save(any(Stamp.class));
//    //        assertThat(축제.getStamps()).extracting("title").contains(새로운_스탬프투어_제목);
//    //    }
//
//    //    @Test
//    //    void 축제에_스탬프투어_생성_실패_축제없음() {
//    //        given(festivalRepository.findById(anyLong())).willReturn(Optional.empty());
//    //
//    //        assertThatThrownBy(() -> service.createStampTourByFestival(1L, "title"))
//    //                .isInstanceOf(FestivalNotFoundException.class);
//    //    }
//
//    @Test
//    void 축제별_스탬프투어_목록_조회_성공() {
//        given(festivalRepository.findById(anyLong())).willReturn(Optional.of(축제));
//
//        var 결과 = service.getStampTourListByFestival(축제_id);
//
//        assertThat(결과).hasSize(1);
//        assertThat(결과.get(0).getTitle()).isEqualTo(스탬프.getTitle());
//    }
//
//    @Test
//    void 축제별_스탬프투어_목록_조회_실패_축제없음() {
//        given(festivalRepository.findById(존재하지_않는_스탬프)).willReturn(Optional.empty());
//
//        assertThatThrownBy(() -> service.getStampTourListByFestival(존재하지_않는_스탬프))
//                .isInstanceOf(FestivalNotFoundException.class);
//    }
//
//    @Test
//    void 기본설정_조회_성공() {
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//
//        var 결과 = service.getStampTourSettingBasicByFestival(축제_id, 스탬프투어1_ID);
//
//        assertThat(결과.getStampId()).isEqualTo(스탬프.getId());
//        assertThat(결과.getTitle()).isEqualTo(스탬프.getTitle());
//    }
//
//    @Test
//    void 기본설정_조회_실패_스탬프없음() {
//        given(stampRepository.findById(존재하지_않는_스탬프)).willReturn(Optional.empty());
//
//        assertThatThrownBy(() -> service.getStampTourSettingBasicByFestival(축제_id, 존재하지_않는_스탬프))
//                .isInstanceOf(StampNotFoundException.class);
//    }
//
//    @Test
//    void 기본설정_조회_실패_축제불일치() {
//        long 다른축제_스탬프_id = 20L;
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(다른축제_스탬프));
//
//        assertThatThrownBy(() -> service.getStampTourSettingBasicByFestival(축제_id, 다른축제_스탬프_id))
//                .isInstanceOf(BaseException.class);
//    }
//
//    @Test
//    void 기본설정_수정_성공() {
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        var 요청 = new StampTourBasicUpdateReqDto(바뀐_스탬프투어_활성화, 바뀐_스탬프투어_제목, 바뀐_스탬프투어_유저인증방법, 바뀐_스탬프투어_관리자비번);
//
//        service.updateBasicSettings(축제_id, 스탬프투어1_ID, 요청);
//
//        assertThat(스탬프.getTitle()).isEqualTo(바뀐_스탬프투어_제목);
//        assertThat(스탬프.getActive()).isFalse();
//        assertThat(스탬프.getPrizeReceiptAuthPassword()).isEqualTo(바뀐_스탬프투어_관리자비번);
//        assertThat(스탬프.getAuthMethod()).isEqualTo(바뀐_스탬프투어_유저인증방법);
//    }
//
//    @Test
//    void 기본설정_수정_실패_스탬프없음() {
//        given(stampRepository.findById(anyLong())).willReturn(Optional.empty());
//        var 요청 = new StampTourBasicUpdateReqDto(바뀐_스탬프투어_활성화, 바뀐_스탬프투어_제목, 바뀐_스탬프투어_유저인증방법, 바뀐_스탬프투어_관리자비번);
//
//        assertThatThrownBy(() -> service.updateBasicSettings(축제_id, 스탬프투어1_ID, 요청))
//                .isInstanceOf(StampNotFoundException.class);
//    }
//
////    @Test
////    void 기본설정_수정_실패_축제불일치() {
////        given(stampRepository.findById(anyLong())).willReturn(Optional.of(다른축제_스탬프));
////        var 요청 = new StampTourBasicUpdateReqDto(바뀐_스탬프투어_활성화, 바뀐_스탬프투어_제목, 바뀐_스탬프투어_유저인증방법, 바뀐_스탬프투어_관리자비번);
////
////        assertThatThrownBy(() -> service.updateBasicSettings(축제_id, 스탬프투어1_ID, 요청))
////                .isInstanceOf(RuntimeException.class);
////    }
//
//    //    @Test
//    //    void 안내사항_조회_성공() {
//    //        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//    //        given(stampNoticeRepository.findAllByStampIdOrderByIdDesc(anyLong())).willReturn(new ArrayList<>());
//    //
//    //        var 결과 = service.getStampTourNotification(축제_id, 스탬프투어1_ID);
//    //        assertThat(결과.getCautionContent()).isEqualTo(공지2_주의사항);
//    //        assertThat(결과.getPersonalInformationContent()).isEqualTo(공지2_개인정보);
//    //    }
//    //
//    //    @Test
//    //    void 안내사항_수정_성공() {
//    //        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//    //
//    //        service.updateStampTourNotification(축제_id, 스탬프투어1_ID, 공지2_주의사항, 공지2_개인정보);
//    //
//    //        assertThat(스탬프.getStampNotices().get(0)).isNotNull();
//    //        assertThat(스탬프.getStampNotices().get(0)).isEqualTo(공지2_주의사항);
//    //        assertThat(스탬프.getStampNotices().get(0)).isEqualTo(공지2_개인정보);
//    //    }
//    //
//    //    @Test
//    //    void 안내사항_수정_실패_스탬프없음() {
//    //        given(stampRepository.findById(anyLong())).willReturn(Optional.empty());
//    //
//    //        assertThatThrownBy(() -> service.updateStampTourNotification(축제_id, 스탬프투어1_ID, 공지2_주의사항, 공지2_개인정보))
//    //                .isInstanceOf(StampNotFoundException.class);
//    //    }
//
//    @Test
//    void 랜딩페이지_설정_조회_성공_템플릿존재() {
//        PageTemplate 랜딩페이지 = 랜딩페이지_생성(스탬프);
//        랜딩페이지.updateLandingPageTemplate(랜딩페이지_요청_생성(TWO_SYM, 버튼2개()));
//
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(pageTemplateRepository.findFirstByStampIdAndType(anyLong(), any(PageType.class)))
//                .willReturn(Optional.of(랜딩페이지));
//
//        var 결과 = service.getLandingPageSettings(축제_id, 스탬프투어1_ID);
//
//        assertThat(결과.getBackgroundImgUrl()).isEqualTo(랜딩페이지.getBackgroundImg());
//        assertThat(결과.getButtonLayout()).isEqualTo(랜딩페이지.getButtonLayout());
//        assertThat(결과.getButtons()).hasSize(2);
//    }
//
//    @Test
//    void 랜딩페이지_설정_조회_성공_템플릿없어_생성() {
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(pageTemplateRepository.findFirstByStampIdAndType(anyLong(), any(PageType.class)))
//                .willReturn(Optional.empty());
//        given(pageTemplateRepository.save(any(PageTemplate.class))).willAnswer(inv -> inv.getArgument(0));
//
//        var 결과 = service.getLandingPageSettings(축제_id, 스탬프투어1_ID);
//
//        assertThat(결과.getLandingPageDesignTemplate()).isEqualTo(LandingPageDesignTemplate.NONE);
//        then(pageTemplateRepository).should().save(any(PageTemplate.class));
//    }
//
//    @Test
//    void 랜딩페이지_설정_수정_성공_템플릿존재() {
//        PageTemplate 랜딩페이지 = 랜딩페이지_생성(스탬프);
//
//        var 요청 = 랜딩페이지_요청_생성(ButtonLayout.TWO_ASYM, 버튼2개());
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(pageTemplateRepository.findFirstByStampIdAndType(anyLong(), any(PageType.class)))
//                .willReturn(Optional.of(랜딩페이지));
//
//        service.updateLandingPage(축제_id, 스탬프투어1_ID, 요청);
//
//        assertThat(랜딩페이지.getLandingPageDesignTemplate()).isEqualTo(요청.getLandingPageDesignTemplate());
//        assertThat(랜딩페이지.getBackgroundImg()).isEqualTo(요청.getBackgroundImgUrl());
//        assertThat(랜딩페이지.getButtons()).hasSize(2);
//    }
//
//    //    @Test
//    //    void 랜딩페이지_설정_수정_성공_템플릿없어_생성() {
//    //        var 요청 = 랜딩페이지_요청_생성(ButtonLayout.ONE, 버튼1개());
//    //
//    //        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//    //        given(pageTemplateRepository.findFirstByStampIdAndType(anyLong(), any(PageType.class)))
//    //                .willReturn(Optional.empty());
//    //        given(pageTemplateRepository.save(any(PageTemplate.class))).willAnswer(inv -> inv.getArgument(0));
//    //
//    //        service.updateLandingPage(축제_id, 스탬프투어1_ID, 요청);
//    //
//    //        then(pageTemplateRepository).should().save(any(PageTemplate.class));
//    //    }
//
//    @Test
//    void 메인페이지_설정_조회_성공_템플릿존재() {
//        PageTemplate 메인페이지 = 메인페이지_생성(스탬프);
//        var 요청 = 메인페이지_요청_생성(TWO_SYM, 버튼2개());
//        메인페이지.updateMainPageTemplate(요청);
//
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(pageTemplateRepository.findFirstByStampIdAndType(anyLong(), any(PageType.class)))
//                .willReturn(Optional.of(메인페이지));
//
//        var 결과 = service.getMainPageSettings(축제_id, 스탬프투어1_ID);
//
//        assertThat(결과.getBackgroundImgUrl()).isEqualTo(메인페이지.getBackgroundImg());
//        assertThat(결과.getButtons()).hasSize(2);
//    }
//
//    @Test
//    void 메인페이지_설정_수정_성공() {
//        PageTemplate 메인페이지 = 메인페이지_생성(스탬프);
//        var 요청 = 메인페이지_요청_생성(ButtonLayout.ONE, 버튼1개());
//
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(pageTemplateRepository.findFirstByStampIdAndType(anyLong(), any(PageType.class)))
//                .willReturn(Optional.of(메인페이지));
//
//        service.updateMainPageSettings(축제_id, 스탬프투어1_ID, 요청);
//
//        assertThat(메인페이지.getMainPageDesignTemplate()).isEqualTo(요청.getMainPageDesignTemplate());
//        assertThat(메인페이지.getButtonLayout()).isEqualTo(요청.getButtonLayout());
//        assertThat(메인페이지.getButtons()).hasSize(1);
//    }
//
//    @Test
//    void 참여가이드_조회_성공_존재() {
//        ParticipateGuide guide = 참여가이드_기본값(스탬프);
//        setField(guide, "id", 100L);
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(participateGuideRepository.findFirstByStampId(anyLong())).willReturn(Optional.of(guide));
//
//        var 결과 = service.getParticipateGuide(축제_id, 스탬프투어1_ID);
//
//        assertThat(결과.getParticipateGuidePages()).hasSize(0);
//    }
//
//    @Test
//    void 참여가이드_조회_성공_없어서_생성() {
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(participateGuideRepository.findFirstByStampId(anyLong())).willReturn(Optional.empty());
//        given(participateGuideRepository.save(any(ParticipateGuide.class))).willAnswer(inv -> {
//            ParticipateGuide g = inv.getArgument(0);
//            setField(g, "id", 100L);
//            return g;
//        });
//        var 결과 = service.getParticipateGuide(축제_id, 스탬프투어1_ID);
//        assertThat(결과).isNotNull();
//        then(participateGuideRepository).should().save(any(ParticipateGuide.class));
//    }
//
//    @Test
//    void 참여가이드_업서트_성공() {
//        ParticipateGuide guide = 참여가이드_기본값(스탬프);
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(participateGuideRepository.findFirstByStampId(anyLong())).willReturn(Optional.of(guide));
//
//        var 요청 = new StampTourParticipateGuideReqDto(GuideDesignTemplate.FULL, GuideSlideMethod.SLIDE);
//
//        service.updateParticipateGuide(축제_id, 스탬프투어1_ID, 요청);
//
//        assertThat(guide.getGuideDesignTemplate()).isEqualTo(GuideDesignTemplate.FULL);
//        assertThat(guide.getGuideSlideMethod()).isEqualTo(GuideSlideMethod.SLIDE);
//    }
//
//    @Test
//    void 참여가이드_업서트_실패_가이드없음() {
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(participateGuideRepository.findFirstByStampId(anyLong())).willReturn(Optional.empty());
//
//        var 요청 = new StampTourParticipateGuideReqDto(GuideDesignTemplate.FULL, GuideSlideMethod.SLIDE);
//
//        assertThatThrownBy(() -> service.updateParticipateGuide(축제_id, 스탬프투어1_ID, 요청))
//                .isInstanceOf(ParticipateGuideNotFoundException.class);
//    }
//
//    @Test
//    void 참여가이드_페이지_삭제_성공() {
//        ParticipateGuidePage page = 참여방법_페이지1(참여가이드_기본값(스탬프));
//        setField(page, "id", 100L);
//
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(participateGuidePageRepository.findById(anyLong())).willReturn(Optional.of(page));
//
//        service.deleteParticipateGuidePage(축제_id, 스탬프투어1_ID, 100L);
//
//        then(participateGuidePageRepository).should().delete(page);
//    }
//
//    @Test
//    void 참여가이드_페이지_삭제_실패_페이지없음() {
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(participateGuidePageRepository.findById(anyLong())).willReturn(Optional.empty());
//
//        assertThatThrownBy(() -> service.deleteParticipateGuidePage(축제_id, 스탬프투어1_ID, 100L))
//                .isInstanceOf(ParticipateGuidePageNotFoundException.class);
//    }
//
//    @Test
//    void 참여가이드_페이지_생성_성공() {
//        ParticipateGuide guide = 참여가이드_기본값(스탬프);
//
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(participateGuideRepository.findFirstByStampId(anyLong())).willReturn(Optional.of(guide));
//
//        var 요청 = new StampTourParticipateGuidePageReqDto(
//                참여방법_페이지1_제목, 참여방법_페이지1_미디어_제공_형식, 참여방법_페이지1_미디어_url, 참여방법_페이지1_요약, 참여방법_페이지1_상세, 참여방법_페이지1_추가);
//
//        service.createParticipateGuidePage(축제_id, 스탬프투어1_ID, 요청);
//
//        assertThat(guide.getParticipateGuidePages()).hasSize(1);
//        assertThat(guide.getParticipateGuidePages().get(0).getTitle()).isEqualTo(참여방법_페이지1_제목);
//    }
//
//    @Test
//    void 참여가이드_페이지_상세_조회_성공() {
//        ParticipateGuidePage page = 참여방법_페이지2(참여가이드_기본값(스탬프));
//        setField(page, "id", 200L);
//
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(participateGuidePageRepository.findById(anyLong())).willReturn(Optional.of(page));
//
//        ParticipateGuidePageDetailsResDto res = service.getParticipateGuidePageDetails(축제_id, 스탬프투어1_ID, 200L);
//
//        assertThat(res.getPageId()).isEqualTo(200L);
//        assertThat(res.getTitle()).isEqualTo(참여방법_페이지2_제목);
//    }
//
//    @Test
//    void 참여가이드_페이지_상세_조회_실패_페이지없음() {
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(participateGuidePageRepository.findById(anyLong())).willReturn(Optional.empty());
//
//        assertThatThrownBy(() -> service.getParticipateGuidePageDetails(축제_id, 스탬프투어1_ID, 200L))
//                .isInstanceOf(ParticipateGuidePageNotFoundException.class);
//    }
//
//    @Test
//    void 참여가이드_페이지_수정_성공() {
//        ParticipateGuidePage page = 참여방법_페이지3(참여가이드_기본값(스탬프));
//        setField(page, "id", 300L);
//
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(participateGuidePageRepository.findById(anyLong())).willReturn(Optional.of(page));
//
//        var 요청 = new StampTourParticipateGuidePageReqDto("수정제목", 참여방법_페이지3_미디어_제공_형식, "수정URL", "요약수정", "상세수정",
// "추가수정");
//
//        service.updateParticipateGuidePage(축제_id, 스탬프투어1_ID, 300L, 요청);
//
//        assertThat(page.getTitle()).isEqualTo("수정제목");
//        assertThat(page.getMediaUrl()).isEqualTo("수정URL");
//        assertThat(page.getSummary()).isEqualTo("요약수정");
//        assertThat(page.getDetails()).isEqualTo("상세수정");
//        assertThat(page.getAdditional()).isEqualTo("추가수정");
//    }
//
//    @Test
//    void 참여가이드_페이지_수정_실패_페이지없음() {
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(participateGuidePageRepository.findById(anyLong())).willReturn(Optional.empty());
//
//        var 요청 = new StampTourParticipateGuidePageReqDto("수정제목", 참여방법_페이지3_미디어_제공_형식, "수정URL", "요약수정", "상세수정",
// "추가수정");
//
//        assertThatThrownBy(() -> service.updateParticipateGuidePage(축제_id, 스탬프투어1_ID, 300L, 요청))
//                .isInstanceOf(ParticipateGuidePageNotFoundException.class);
//    }
//
//    @Test
//    void 참여가이드_페이지_노출순서_수정_성공() {
//        ParticipateGuide guide = 참여가이드_기본값(스탬프);
//        var pages = 페이지_리스트_생성(guide);
//
//        long id = 1000L;
//        for (ParticipateGuidePage p : pages) {
//            setField(p, "id", id++);
//        }
//
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(participateGuideRepository.findFirstByStampId(anyLong())).willReturn(Optional.of(guide));
//
//        var 순서 = List.of(
//                OrderUpdateRequest.of(pages.get(4).getId(), 1),
//                OrderUpdateRequest.of(pages.get(3).getId(), 2),
//                OrderUpdateRequest.of(pages.get(2).getId(), 3),
//                OrderUpdateRequest.of(pages.get(1).getId(), 4),
//                OrderUpdateRequest.of(pages.get(0).getId(), 5));
//
//        List<ParticipateGuidePageSummaryResDto> res = service.updateDisplayOrder(축제_id, 스탬프투어1_ID, 순서);
//
//        assertThat(res).hasSize(5);
//        assertThat(res.stream().map(ParticipateGuidePageSummaryResDto::getDisplayOrder))
//                .containsExactlyInAnyOrder(1, 2, 3, 4, 5);
//    }
//
//    @Test
//    void 참여가이드_페이지_노출순서_수정_실패_가이드없음() {
//        given(stampRepository.findById(anyLong())).willReturn(Optional.of(스탬프));
//        given(participateGuideRepository.findFirstByStampId(anyLong())).willReturn(Optional.empty());
//
//        assertThatThrownBy(() -> service.updateDisplayOrder(축제_id, 스탬프투어1_ID, List.of()))
//                .isInstanceOf(ParticipateGuideNotFoundException.class);
//    }
//
//    @Test
//    void 메인페이지_설정_조회_실패_스탬프없음() {
//        given(stampRepository.findById(anyLong())).willReturn(Optional.empty());
//        assertThatThrownBy(() -> service.getMainPageSettings(축제_id, 스탬프투어1_ID))
//                .isInstanceOf(StampNotFoundException.class);
//    }
// }
