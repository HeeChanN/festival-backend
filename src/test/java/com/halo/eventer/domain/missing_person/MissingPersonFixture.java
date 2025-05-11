package com.halo.eventer.domain.missing_person;

import com.halo.eventer.domain.missing_person.dto.MissingPersonReqDto;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class MissingPersonFixture {

    public static MissingPersonReqDto 실종자_생성_DTO() {
        MissingPersonReqDto missingPersonReqDto = new MissingPersonReqDto();
        setField(missingPersonReqDto, "name", "name");
        setField(missingPersonReqDto, "age", "age");
        setField(missingPersonReqDto, "gender", "gender");
        setField(missingPersonReqDto, "thumbnail", "thumbnail");
        setField(missingPersonReqDto, "missingLocation", "missingLocation");
        setField(missingPersonReqDto, "missingTime", "missingTime");
        setField(missingPersonReqDto, "content", "content");
        setField(missingPersonReqDto, "parentName", "parentName");
        setField(missingPersonReqDto, "parentNo", "parentNo");
        setField(missingPersonReqDto, "domainUrlName", "domainUrlName");
        return missingPersonReqDto;
    }
}
