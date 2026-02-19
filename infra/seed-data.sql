-- ============================================================
-- Festimap 부하 테스트용 시드 데이터
-- 메인 홈페이지(/home/{festivalId}) 조회에 필요한 데이터만 적재
-- 각 엔티티 5건씩
-- ============================================================

-- 1. Festival (1건 - 모든 데이터의 FK)
INSERT INTO festival (id, name, sub_address, main_color, sub_color, font_color, background_color,
                      logo, latitude, longitude, sido, sigungu, dongmyun, road_name, road_number, building_name)
VALUES (1, '2026 봄 축제', 'spring-2026', '#FF6B35', '#FFFFFF', '#1A1A1A', '#F8F9FA',
        'https://placehold.co/200x200?text=Logo', 37.5665, 126.9780,
        '서울특별시', '강남구', '역삼동', '테헤란로', '142', '축제홀');

-- 2. UP Widget (5건 - 상단 배너, 기간 필터링 있음 → 현재 시점 포함하도록 설정)
INSERT INTO widget (festival_id, name, url, widget_type, display_order, properties, created_at, updated_at) VALUES
(1, '긴급 공지',       'https://example.com/urgent',   'UP', 0, '{"type":"UP","periodStart":"2025-01-01T00:00:00","periodEnd":"2027-12-31T23:59:59"}', NOW(), NOW()),
(1, '라인업 안내',     'https://example.com/lineup',   'UP', 0, '{"type":"UP","periodStart":"2025-01-01T00:00:00","periodEnd":"2027-12-31T23:59:59"}', NOW(), NOW()),
(1, '주차 안내',       'https://example.com/parking',  'UP', 0, '{"type":"UP","periodStart":"2025-01-01T00:00:00","periodEnd":"2027-12-31T23:59:59"}', NOW(), NOW()),
(1, '날씨 알림',       'https://example.com/weather',  'UP', 0, '{"type":"UP","periodStart":"2025-01-01T00:00:00","periodEnd":"2027-12-31T23:59:59"}', NOW(), NOW()),
(1, '실시간 혼잡도',   'https://example.com/crowd',    'UP', 0, '{"type":"UP","periodStart":"2025-01-01T00:00:00","periodEnd":"2027-12-31T23:59:59"}', NOW(), NOW());

-- 3. MAIN Widget (5건 - 메인 콘텐츠)
INSERT INTO widget (festival_id, name, url, widget_type, display_order, properties, created_at, updated_at) VALUES
(1, '메인 스테이지',   'https://example.com/stage',    'MAIN', 0, '{"type":"MAIN","image":"https://placehold.co/600x400?text=Main+Stage","description":"메인 스테이지 라이브 공연"}', NOW(), NOW()),
(1, '푸드코트',       'https://example.com/food',     'MAIN', 0, '{"type":"MAIN","image":"https://placehold.co/600x400?text=Food+Court","description":"전국 맛집 한자리에"}', NOW(), NOW()),
(1, '체험 부스',       'https://example.com/booth',    'MAIN', 0, '{"type":"MAIN","image":"https://placehold.co/600x400?text=Booth","description":"다양한 체험 프로그램"}', NOW(), NOW()),
(1, '기념품샵',       'https://example.com/shop',     'MAIN', 0, '{"type":"MAIN","image":"https://placehold.co/600x400?text=Shop","description":"한정판 기념품 판매"}', NOW(), NOW()),
(1, '포토존',         'https://example.com/photo',    'MAIN', 0, '{"type":"MAIN","image":"https://placehold.co/600x400?text=Photo+Zone","description":"인생 사진 남기기"}', NOW(), NOW());

-- 4. MIDDLE Widget (5건 - 중간 배너)
INSERT INTO widget (festival_id, name, url, widget_type, display_order, properties, created_at, updated_at) VALUES
(1, '이벤트 1',       'https://example.com/event1',   'MIDDLE', 1, '{"type":"MIDDLE","image":"https://placehold.co/800x200?text=Event+1"}', NOW(), NOW()),
(1, '이벤트 2',       'https://example.com/event2',   'MIDDLE', 2, '{"type":"MIDDLE","image":"https://placehold.co/800x200?text=Event+2"}', NOW(), NOW()),
(1, '이벤트 3',       'https://example.com/event3',   'MIDDLE', 3, '{"type":"MIDDLE","image":"https://placehold.co/800x200?text=Event+3"}', NOW(), NOW()),
(1, '이벤트 4',       'https://example.com/event4',   'MIDDLE', 4, '{"type":"MIDDLE","image":"https://placehold.co/800x200?text=Event+4"}', NOW(), NOW()),
(1, '이벤트 5',       'https://example.com/event5',   'MIDDLE', 5, '{"type":"MIDDLE","image":"https://placehold.co/800x200?text=Event+5"}', NOW(), NOW());

-- 5. SQUARE Widget (5건 - 정사각형 아이콘)
INSERT INTO widget (festival_id, name, url, widget_type, display_order, properties, created_at, updated_at) VALUES
(1, '지도',           'https://example.com/map',      'SQUARE', 1, '{"type":"SQUARE","image":"https://placehold.co/100x100?text=Map","description":"축제장 지도"}', NOW(), NOW()),
(1, '타임테이블',     'https://example.com/schedule', 'SQUARE', 2, '{"type":"SQUARE","image":"https://placehold.co/100x100?text=Schedule","description":"공연 시간표"}', NOW(), NOW()),
(1, '스탬프투어',     'https://example.com/stamp',    'SQUARE', 3, '{"type":"SQUARE","image":"https://placehold.co/100x100?text=Stamp","description":"스탬프 투어"}', NOW(), NOW()),
(1, '분실물',         'https://example.com/lost',     'SQUARE', 4, '{"type":"SQUARE","image":"https://placehold.co/100x100?text=Lost","description":"분실물 센터"}', NOW(), NOW()),
(1, '문의하기',       'https://example.com/inquiry',  'SQUARE', 5, '{"type":"SQUARE","image":"https://placehold.co/100x100?text=QnA","description":"문의 접수"}', NOW(), NOW());

-- 6. DOWN Widget (5건 - 하단 배너)
INSERT INTO widget (festival_id, name, url, widget_type, display_order, properties, created_at, updated_at) VALUES
(1, '후원사 A',       'https://example.com/sponsor-a', 'DOWN', 1, '{"type":"DOWN"}', NOW(), NOW()),
(1, '후원사 B',       'https://example.com/sponsor-b', 'DOWN', 2, '{"type":"DOWN"}', NOW(), NOW()),
(1, '후원사 C',       'https://example.com/sponsor-c', 'DOWN', 3, '{"type":"DOWN"}', NOW(), NOW()),
(1, '후원사 D',       'https://example.com/sponsor-d', 'DOWN', 4, '{"type":"DOWN"}', NOW(), NOW()),
(1, '후원사 E',       'https://example.com/sponsor-e', 'DOWN', 5, '{"type":"DOWN"}', NOW(), NOW());

-- 7. Notice (5건 - picked=true → 홈 배너에 표시)
INSERT INTO notice (festival_id, tag, title, writer, content, thumbnail, type, picked, display_order, created_at, updated_at) VALUES
(1, '공지',   '축제 오시는 길 안내',        '운영팀', '셔틀버스 운행 안내입니다.',         'https://placehold.co/400x200?text=Banner+1', 'NOTICE', true, 1, NOW(), NOW()),
(1, '이벤트', '스탬프 투어 경품 추첨',       '운영팀', '스탬프 투어 참여하고 경품 받자!',   'https://placehold.co/400x200?text=Banner+2', 'EVENT',  true, 2, NOW(), NOW()),
(1, '공지',   '무료 주차장 안내',           '운영팀', '주차장 위치와 이용 방법입니다.',     'https://placehold.co/400x200?text=Banner+3', 'NOTICE', true, 3, NOW(), NOW()),
(1, '이벤트', 'SNS 인증샷 이벤트',          '운영팀', 'SNS에 인증샷 올리고 선물 받자!',    'https://placehold.co/400x200?text=Banner+4', 'EVENT',  true, 4, NOW(), NOW()),
(1, '공지',   '우천 시 프로그램 변경 안내',   '운영팀', '우천 시 일부 프로그램이 변경됩니다.', 'https://placehold.co/400x200?text=Banner+5', 'NOTICE', true, 5, NOW(), NOW());

-- 8. Missing Person (5건 - popup=true → 홈 팝업에 표시)
INSERT INTO missing_person (festival_id, name, age, gender, thumbnail, missing_location, missing_time, content, parent_name, parent_no, popup) VALUES
(1, '김민서', '7',  '여', 'https://placehold.co/100x100?text=1', '메인 스테이지 앞',   '14:00', '빨간 원피스를 입고 있습니다',      '김영수', '010-1234-0001', true),
(1, '이준호', '5',  '남', 'https://placehold.co/100x100?text=2', '푸드코트 근처',       '15:30', '파란 티셔츠에 검은 반바지',         '이지영', '010-1234-0002', true),
(1, '박서윤', '8',  '여', 'https://placehold.co/100x100?text=3', '체험부스 3번',        '13:00', '노란 모자를 쓰고 있습니다',         '박철호', '010-1234-0003', true),
(1, '최도윤', '6',  '남', 'https://placehold.co/100x100?text=4', '기념품샵 입구',       '16:00', '회색 후드티를 입고 있습니다',        '최미라', '010-1234-0004', true),
(1, '정하은', '9',  '여', 'https://placehold.co/100x100?text=5', '포토존 근처',         '11:30', '분홍색 가방을 메고 있습니다',        '정태우', '010-1234-0005', true);
