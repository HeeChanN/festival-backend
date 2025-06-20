package com.halo.eventer.domain.image.entity;

import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.image.Image;
import com.halo.eventer.domain.notice.Notice;
import com.halo.eventer.domain.widget_item.WidgetItem;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

@SuppressWarnings("NonAsciiCharacters")
public class ImageTest {

    @Test
    void ofNotice_생성_테스트() {
        // given
        Notice notice = mock(Notice.class);
        String imageUrl = "url";

        // when
        Image result = Image.ofNotice(imageUrl, notice);

        // then
        assertThat(result.getImageUrl()).isEqualTo(imageUrl);
        assertThat(result.getNotice()).isEqualTo(notice);
        assertThat(result.getWidgetItem()).isNull();
    }

    @Test
    void ofWidgetItem_생성_테스트() {
        // given
        String imageUrl = "url";
        WidgetItem widgetItem = mock(WidgetItem.class);

        // when
        Image image = Image.ofWidgetItem(imageUrl, widgetItem);

        // then
        assertThat(image.getImageUrl()).isEqualTo(imageUrl);
        assertThat(image.getNotice()).isNull();
        assertThat(image.getWidgetItem()).isEqualTo(widgetItem);
    }
}
