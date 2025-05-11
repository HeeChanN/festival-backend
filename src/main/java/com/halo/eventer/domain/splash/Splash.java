package com.halo.eventer.domain.splash;

import javax.persistence.*;

import com.halo.eventer.domain.festival.Festival;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Splash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String backgroundImage;

    private String topLayerImage;

    private String centerLayerImage;

    private String bottomLayerImage;

    @ManyToOne
    private Festival festival;

    public Splash(Festival festival) {
        this.festival = festival;
    }

    public void setBackgroundImage(String url) {
        this.backgroundImage = url;
    }

    public void setTopLayerImage(String url) {
        this.topLayerImage = url;
    }

    public void setCenterLayerImage(String url) {
        this.centerLayerImage = url;
    }

    public void setBottomLayerImage(String url) {
        this.bottomLayerImage = url;
    }
}
