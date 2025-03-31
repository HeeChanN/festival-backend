package com.halo.eventer.domain.map.repository;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.dto.FestivalCreateDto;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.map.MapCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Disabled()
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SuppressWarnings("NonAsciiCharacters")
public class MapCategoryRepositoryTest {

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private MapCategoryRepository mapCategoryRepository;

    private Festival festival;
    private MapCategory mapCategory;

    @BeforeEach
    void setUp(){
        FestivalCreateDto dto = new FestivalCreateDto("축제","univ");
        festival = festivalRepository.save(Festival.from(dto));
        mapCategory = MapCategory.of(festival,"카테고리");
        mapCategoryRepository.save(mapCategory);
    }

    @Test
    void 지도_카테고리_생성(){
        assertThat(mapCategory.getCategoryName()).isEqualTo("카테고리");
        assertThat(mapCategory.getFestival().getName()).isEqualTo("축제");
        assertThat(mapCategory.getFestival().getSubAddress()).isEqualTo("univ");
    }

    @Test
    void 축제id로_지도_카테고리_리스트_조회(){
        //when
        List<MapCategory> mapCategories = mapCategoryRepository.findAllByFestivalId(festival.getId());

        //then
        assertThat(mapCategories).hasSize(1);
        assertThat(mapCategories.get(0).getCategoryName()).isEqualTo("카테고리");
    }
}
