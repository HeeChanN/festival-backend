package com.halo.eventer.global.constants;

public class SecurityConstants {

    // 공개 API 경로
    public static final String[] SWAGGER_URLS = {
        "/", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/docs/**"
    };

    public static final String[] PUBLIC_GET_URLS = {
        "/concert",
        "/concert/*",
        "/notice/banner",
        "/notice/**",
        "/widget/*",
        "/widget",
        "/stamp",
        "/stamp/users",
        "/stamp/missions",
        "/stamp/mission",
        "/stamp/mission/all",
        "/splash",
        "/missingPerson",
        "/missingPerson/*",
        "/middleBanner",
        "/middleBanner/*",
        "/menu",
        "/maps",
        "/maps/*",
        "/map-categories/*/maps",
        "/*/map-categories",
        "/map-categories",
        "/map-categories/*",
        "/map-categories/*/image",
        "/menuCategory/image",
        "*/map-categories/displayOrder",
        "/manager",
        "/*/lost-items",
        "/lost-items/*",
        "/inquiries/paging",
        "/inquiries/forUser",
        "/festivals",
        "/festivals/**",
        "/duration/*",
        "/duration",
        "/concertInfo",
        "/concertInfo/*",
        "/api/upWidgets/datetime",
        "/univ",
        "/home/*",
        "/home",
        "/notices/*",
        "/notices",
        "/maps/*/menus",
        "/test/festival",
        "/upload/preSigned",
    };

    public static final String[] PUBLIC_POST_URLS = {
        "/stamp/user",
        "/stamp/mission",
        "/stamp/user/login",
        "/stamp/user/custom",
        "/missingPerson",
        "/login",
        "/inquiries",
        "/inquiries/forUser/*",
    };

    public static final String[] PUBLIC_PATCH_URLS = {"/stamp/user/*/*", "/stamp/mission"};

    public static final String[] ACTUATOR_URL = {"/actuator/prometheus"};
}
