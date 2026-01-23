package com.halo.eventer.domain.stamp.repository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.StampUser;

import static com.halo.eventer.domain.festival.FestivalFixture.축제_엔티티;
import static com.halo.eventer.domain.stamp.fixture.StampUserFixture.*;

@DataJpaTest
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
public class StampUserRepositoryTest {

    @Autowired
    private StampUserRepository stampUserRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private StampRepository stampRepository;

    private StampUser stampUser;
    private Stamp stamp;
    private Festival festival;

    @BeforeEach
    void setUp() {
        festival = 축제_엔티티();
        festivalRepository.save(festival);
        stamp = Stamp.createWith(festival, "title", true);
        stampRepository.save(stamp);
        stampUser = 스탬프유저1_생성();
        stampUser.addStamp(stamp);
        stampUserRepository.save(stampUser);
    }
    //
    //    @Test
    //    void uuid로_스탬프유저_찾기() {
    //        // when
    //        Optional<StampUser> result = stampUserRepository.findByUuid(stampUser.getUuid());
    //        // then
    //        assertThat(result).isPresent();
    //        assertThat(result.get().getId()).isEqualTo(stampUser.getId());
    //        assertThat(result.get().getUuid()).isEqualTo(stampUser.getUuid());
    //    }
    //
    //    @Test
    //    void 스탬프ID와_전화번호로_스탬프유저_존재확인() {
    //        // when
    //        boolean result = stampUserRepository.existsByStampIdAndPhone(stamp.getId(), 암호화된_번호);
    //
    //        // then
    //        assertThat(result).isTrue();
    //    }
    //
    //    @Test
    //    void 스탬프ID와_전화번호로_스탬프유저_존재X() {
    //        // when
    //        boolean result = stampUserRepository.existsByStampIdAndPhone(stamp.getId(), "wrongPhoneNum");
    //
    //        // then
    //        assertThat(result).isFalse();
    //    }
    //
    //    @Test
    //    void 스탬프ID_전화번호_이름으로_스탬프유저_조회() {
    //        // when
    //        Optional<StampUser> result = stampUserRepository.findByStampIdAndPhoneAndName(stamp.getId(), 암호화된_번호,
    // 암호화된_이름);
    //
    //        // then
    //        assertThat(result).isPresent();
    //    }
    //
    //    @Test
    //    void 스탬프ID_전화번호_이름으로_스탬프유저_조회X() {
    //        // when
    //        Optional<StampUser> result =
    //                stampUserRepository.findByStampIdAndPhoneAndName(stamp.getId(), "aa", "encryptedName");
    //
    //        // then
    //        assertThat(result).isEmpty();
    //    }
}
