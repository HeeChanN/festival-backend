package com.halo.eventer.domain.stamp.fixture;

import java.util.List;

import com.halo.eventer.domain.stamp.StampUser;
import com.halo.eventer.domain.stamp.dto.stamp.StampUsersGetDto;
import com.halo.eventer.domain.stamp.dto.stampUser.*;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class StampUserFixture {

    public static final String 암호화된_번호 = "encrypted phone";
    public static final String 암호화된_이름 = "encrypted name";

    public static final String TEST_UUID = "123e4567-e89b-12d3-a456-426614174000";

    public static final Long 유저1_ID = 1L;
    public static final String 유저1_UUID = "123e4567-e89b-12d3-a456-4266141TEST2";
    public static final String 유저1_이름 = "홍길동";
    public static final String 유저1_전번 = "01012345678";
    public static final int 유저1_참여_갯수 = 1;
    public static final boolean 유저1_완료여부 = false;

    public static final Long 유저2_ID = 2L;
    public static final String 유저2_UUID = "123e4567-e89b-12d3-a456-4266141TEST1";
    public static final String 유저2_이름 = "김철수";
    public static final String 유저2_전번 = "01098765432";
    public static final boolean 유저2_완료여부 = true;
    public static final int 유저2_참여_갯수 = 2;

    public static StampUser 스탬프유저1_생성() {
        return new StampUser(암호화된_번호, 암호화된_이름, 1);
    }

    public static SignupDto 회원가입_DTO_생성() {
        SignupDto signupDto = new SignupDto();
        setField(signupDto, "name", "test name");
        setField(signupDto, "phone", "test phone");
        setField(signupDto, "participantCount", 10);
        return signupDto;
    }

    public static SignupWithoutCustomDto 커스텀_없는_회원가입_DTO_생성() {
        SignupWithoutCustomDto signupDto = new SignupWithoutCustomDto();
        setField(signupDto, "name", "test name");
        setField(signupDto, "phone", "test phone");
        setField(signupDto, "participantCount", 10);
        return signupDto;
    }

    public static SignupWithCustomDto 커스텀_있는_회원가입_DTO_생성() {
        SignupWithCustomDto signupDto = new SignupWithCustomDto();
        setField(signupDto, "name", "test name");
        setField(signupDto, "phone", "test phone");
        setField(signupDto, "participantCount", 10);
        setField(signupDto, "schoolNo", "test school no");
        return signupDto;
    }

    public static LoginDto 로그인_DTO_생성() {
        LoginDto loginDto = new LoginDto();
        setField(loginDto, "name", "test");
        setField(loginDto, "phone", "01012341234");
        return loginDto;
    }

    public static StampUserGetDto 스탬프유저_응답_DTO_생성() {
        List<UserMissionInfoGetDto> missionDtos =
                List.of(new UserMissionInfoGetDto(1L, 11L, false), new UserMissionInfoGetDto(2L, 12L, true));
        return new StampUserGetDto(TEST_UUID, false, 10, missionDtos);
    }

    public static SignupDto 유효하지_않는_회원가입_DTO_생성() {
        SignupDto signupDto = new SignupDto();
        setField(signupDto, "name", "");
        setField(signupDto, "phone", "");
        setField(signupDto, "participantCount", 0);
        return signupDto;
    }

    public static SignupDto 유효하지_않는_커스텀_회원가입_DTO_생성() {
        SignupDto signupDto = new SignupDto();
        setField(signupDto, "name", "");
        setField(signupDto, "phone", "");
        setField(signupDto, "participantCount", 0);
        setField(signupDto, "schoolNo", "");
        return signupDto;
    }

    public static List<StampUsersGetDto> 스탬프유저들_응답_DTO_생성() {
        return List.of(
                new StampUsersGetDto(유저1_UUID, 유저1_이름, 유저1_전번, 유저1_완료여부, 유저1_참여_갯수),
                new StampUsersGetDto(유저2_UUID, 유저2_이름, 유저2_전번, 유저2_완료여부, 유저2_참여_갯수));
    }
}
