package com.halo.eventer.domain.image.service;

import java.net.URL;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.BaseException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ImageServiceTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Presigner presigner;

    @Mock
    private PresignedPutObjectRequest presignedRequest;

    @InjectMocks
    private ImageService imageService;

    @Test
    void 허용된_확장자일_경우_presigned_url_발급() throws Exception {
        // given
        String validExtension = "jpg";
        URL mockUrl = new URL("https://test.s3.amazonaws.com/test-image.jpg");
        given(presigner.presignPutObject(any(PutObjectPresignRequest.class))).willReturn(presignedRequest);
        given(presignedRequest.url()).willReturn(mockUrl);

        // when
        String result = imageService.generatePreSignedUploadUrl(validExtension);

        // then
        assertThat(result).isEqualTo(mockUrl.toString());
    }

    @Test
    void 허용되지_않은_확장자일_경우_예외() {
        // given
        String invalidExtension = "gif";

        // when & then
        assertThatThrownBy(() -> imageService.generatePreSignedUploadUrl(invalidExtension))
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.UNACCEPTABLE_EXTENSION);
    }
}
