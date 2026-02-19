import http from 'k6/http';
import { check, sleep } from 'k6';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.2/index.js';

/**
 * 시나리오 1: 점진적 부하 테스트
 *
 * 200 VU → 1000 VU까지 점진적으로 증가시켜 축제 피크 트래픽을 시뮬레이션.
 * Baseline / Phase 1 / Phase 2 모두 이 스크립트로 테스트.
 *
 * 사용법:
 *   k6 run baseline-test.js -e TARGET_HOST=<서버주소> -e FESTIVAL_ID=1
 */

const TARGET_HOST = __ENV.TARGET_HOST || 'localhost:8080';
const FESTIVAL_ID = __ENV.FESTIVAL_ID || '1';

export const options = {
    stages: [
        { duration: '30s', target: 200 },   // 0→200 VU 램프업
        { duration: '2m',  target: 200 },  // 200 VU 유지
        { duration: '30s', target: 1000 }, // 200→1000 VU 램프업
        { duration: '2m',  target: 1000 }, // 1000 VU 유지 (피크)
        { duration: '30s', target: 0 },    // 램프다운
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'],   // p95 < 500ms
        http_req_failed: ['rate<0.01'],     // 에러율 < 1%
    },
};

export default function () {
    const res = http.get(`http://${TARGET_HOST}/home/${FESTIVAL_ID}`);

    check(res, {
        'status is 200': (r) => r.status === 200,
        'response has body': (r) => r.body && r.body.length > 0,
    });

    sleep(0.1); // 100ms 간격 (VU당 초당 ~10회 요청)
}

export function handleSummary(data) {
    const phase = __ENV.PHASE || 'baseline';
    return {
        [`results-${phase}.json`]: JSON.stringify(data, null, 2),
        stdout: textSummary(data, { indent: '  ', enableColors: true }),
    };
}
