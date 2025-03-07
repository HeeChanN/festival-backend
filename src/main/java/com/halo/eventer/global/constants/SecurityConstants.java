package com.halo.eventer.global.constants;

public class SecurityConstants {

    // 공개 API 경로
    public static final String[] SWAGGER_URLS = {
            "/",
            "/swagger-ui/**",
            "/v3/**",
            "/swagger-ui.html",

    };

    public static final String[] PUBLIC_GET_URLS ={
            "/concert",
            "/concert/*",
            "/notice/banner",
            "/notice/**",
            "/widget/*",
            "/widget",
            "/stamp",
            "/stamp/missions",
            "/stamp/mission",
            "/splash",
            "/missingPerson",
            "/missingPerson/*",
            "/middleBanner",
            "/middleBanner/*",
            "/menu",
            "/map",
            "/map/*",
            "/mapCategory",
            "/mapCategory/*",
            "/menuCategory/image",
            "/manager",
            "/lostItem",
            "/lostItem/*",
            "/inquiry/paging",
            "/inquiry/forUser",
            "/festival",
            "/festival/**",
            "/duration/*",
            "/concertInfo",
            "/concertInfo/*",
            "/api/upWidgets",
            "/api/upWidgets/*",
            "/api/upWidgets/datetime",
            "/api/downWidget",
            "/univ",
            "/home/*",
    };

    public static final String[] PUBLIC_POST_URLS = {
            "/stamp/user",
            "/stamp/user/login",
            "/stamp/user/custom",
            "/missingPerson",
            "/login",
            "/inquiry",
            "/inquiry/forUser/*",
    };

    public static final String[] PUBLIC_PATCH_URLS = {
            "/stamp/user/*/*",
    };

    public static final String[] ACTUATOR_URL = {
            "/actuator/prometheus"
    };

}
