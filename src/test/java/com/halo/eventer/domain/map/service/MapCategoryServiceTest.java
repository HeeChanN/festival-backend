package com.halo.eventer.domain.map.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.dto.FestivalCreateDto;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.map.MapCategory;
import com.halo.eventer.domain.map.dto.mapcategory.MapCategoryImageDto;
import com.halo.eventer.domain.map.dto.mapcategory.MapCategoryResDto;
import com.halo.eventer.domain.map.exception.MapCategoryNotFoundException;
import com.halo.eventer.domain.map.repository.MapCategoryRepository;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class MapCategoryServiceTest {

    @Mock
    private MapCategoryRepository mapCategoryRepository;

    @Mock
    private FestivalRepository festivalRepository;

    @InjectMocks
    private MapCategoryService mapCategoryService;

    private Festival festival;
    private MapCategory mapCategory;
    private final Long festivalId = 1L;
    private final Long mapCategoryId = 1L;

    @BeforeEach
    void setUp() {
        FestivalCreateDto festivalCreateDto = new FestivalCreateDto("이름", "주소");
        MapCategoryImageDto imageDto = MapCategoryImageDto.of("핀", "아이콘");
        festival = Festival.from(festivalCreateDto);
        mapCategory = MapCategory.of(festival, "카테고리");
        mapCategory.updateIconAndPin(imageDto);
    }

    @Test
    void 지도_카테고리_생성_성공() {
        // given
        given(festivalRepository.findByIdWithMapCategories(festivalId)).willReturn(Optional.of(festival));
        given(mapCategoryRepository.save(any())).willReturn(any());

        // when
        mapCategoryService.create(festivalId, "카테고리");

        // then
        verify(festivalRepository, times(1)).findByIdWithMapCategories(festivalId);
        verify(mapCategoryRepository, times(1)).save(any());
    }

    @Test
    void 지도_카테고리_생성_축제_없을때_예외() {
        // given
        given(festivalRepository.findByIdWithMapCategories(festivalId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mapCategoryService.create(festivalId, "카테고리"))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 지도_카테고리_리스트_조회() {
        // given
        given(mapCategoryRepository.findAllByFestivalId(festivalId)).willReturn(List.of(mapCategory));

        // when
        List<MapCategoryResDto> targets = mapCategoryService.getMapCategories(festivalId);

        // then
        assertThat(targets).isNotEmpty();
        assertThat(targets.size()).isEqualTo(1);
        assertThat(targets.get(0).getCategoryName()).isEqualTo(mapCategory.getCategoryName());
    }

    @Test
    void 아이콘_핀_조회() {
        // given
        given(mapCategoryRepository.findById(mapCategoryId)).willReturn(Optional.of(mapCategory));

        // when
        MapCategoryImageDto target = mapCategoryService.getIconAndPin(mapCategoryId);

        // then
        assertThat(target.getIcon()).isEqualTo(mapCategory.getIcon());
        assertThat(target.getPin()).isEqualTo(mapCategory.getPin());
    }

    @Test
    void 아이콘_핀_조회_카테고리_없을때_예외() {
        // given
        given(mapCategoryRepository.findById(mapCategoryId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mapCategoryService.getIconAndPin(festivalId))
                .isInstanceOf(MapCategoryNotFoundException.class);
    }

    @Test
    void 아이콘_핀_추가() {
        // given
        given(mapCategoryRepository.findById(mapCategoryId)).willReturn(Optional.of(mapCategory));
        MapCategoryImageDto dto = MapCategoryImageDto.of("핀2", "아이콘2");

        // when
        mapCategoryService.updateIconAndPin(mapCategoryId, dto);

        // then
        assertThat(mapCategory.getPin()).isEqualTo(dto.getPin());
        assertThat(mapCategory.getIcon()).isEqualTo(dto.getIcon());
    }

    @Test
    void 지도_카테고리_삭제() {
        doNothing().when(mapCategoryRepository).deleteById(anyLong());

        // when
        mapCategoryService.delete(mapCategoryId);

        // then
        verify(mapCategoryRepository, times(1)).deleteById(mapCategoryId);
    }

    @Test
    void 카테고리_순서변경() {
        // given
        List<MapCategory> mapCategories = makeMapCategories();
        List<OrderUpdateRequest> orderUpdateRequests = makeOrderRequests();
        given(mapCategoryRepository.findAllByFestivalId(festivalId)).willReturn(mapCategories);

        // when
        List<MapCategoryResDto> targets = mapCategoryService.updateDisplayOrder(festivalId, orderUpdateRequests);

        // then
        assertThat(targets).isNotEmpty();
        assertThat(targets.size()).isEqualTo(3);
        assertThat(targets.get(0).getDisplayOrder())
                .isEqualTo(orderUpdateRequests.get(0).getDisplayOrder());
        assertThat(targets.get(1).getDisplayOrder())
                .isEqualTo(orderUpdateRequests.get(1).getDisplayOrder());
        assertThat(targets.get(2).getDisplayOrder())
                .isEqualTo(orderUpdateRequests.get(2).getDisplayOrder());
    }

    private List<MapCategory> makeMapCategories() {
        List<MapCategory> mapCategories = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MapCategory category = MapCategory.of(festival, "카테고리" + i);
            ReflectionTestUtils.setField(category, "id", i + 1L);
            mapCategories.add(category);
        }
        return mapCategories;
    }

    private List<OrderUpdateRequest> makeOrderRequests() {
        List<OrderUpdateRequest> orderUpdateRequests = new ArrayList<>();
        orderUpdateRequests.add(OrderUpdateRequest.of(1L, 1));
        orderUpdateRequests.add(OrderUpdateRequest.of(2L, 2));
        orderUpdateRequests.add(OrderUpdateRequest.of(3L, 3));
        return orderUpdateRequests;
    }
}
