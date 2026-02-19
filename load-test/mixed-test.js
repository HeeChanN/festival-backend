import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.2/index.js';

/**
 * 읽기/쓰기 혼합 부하 테스트
 *
 * readers: 1000 VU 피크 읽기 부하 (기존 baseline-test.js와 동일 패턴)
 * writers: 5초마다 1회 캐시 무효화 (constant-arrival-rate)
 *
 * Phase 1 (Redis 글로벌 캐시) vs Phase 2 (Caffeine + Pub/Sub) 비교용.
 * 캐시 무효화가 읽기 성능에 미치는 영향을 측정한다.
 *
 * 사용법:
 *   K6_WEB_DASHBOARD=true K6_WEB_DASHBOARD_EXPORT=report.html \
 *   k6 run mixed-test.js \
 *     -e TARGET_HOST=<ALB주소> \
 *     -e FESTIVAL_ID=1 \
 *     -e ADMIN_TOKEN=<JWT토큰>
 */

const TARGET_HOST = __ENV.TARGET_HOST || 'localhost:8080';
const FESTIVAL_ID = __ENV.FESTIVAL_ID || '1';
const ADMIN_TOKEN = __ENV.ADMIN_TOKEN || '';

const evictTotal = new Counter('evict_total');
const evictSuccess = new Counter('evict_success');

export const options = {
    scenarios: {
        readers: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '30s', target: 200 },
                { duration: '2m',  target: 200 },
                { duration: '30s', target: 1000 },
                { duration: '2m',  target: 1000 },
                { duration: '30s', target: 0 },
            ],
            exec: 'readHome',
        },
        writers: {
            executor: 'constant-arrival-rate',
            rate: 12,
            timeUnit: '1m',
            duration: '5m30s',
            preAllocatedVUs: 2,
            maxVUs: 5,
            exec: 'evictCache',
        },
    },
    thresholds: {
        http_req_duration: ['p(95)<500'],
        http_req_failed: ['rate<0.01'],
    },
};

export function readHome() {
    const res = http.get(`http://${TARGET_HOST}/home/${FESTIVAL_ID}`);
    check(res, {
        'read status 200': (r) => r.status === 200,
        'read has body': (r) => r.body && r.body.length > 0,
    });
    sleep(0.1);
}

export function evictCache() {
    evictTotal.add(1);
    const res = http.post(
        `http://${TARGET_HOST}/admin/cache/evict/home/${FESTIVAL_ID}`,
        null,
        {
            headers: {
                'Authorization': `BEARER ${ADMIN_TOKEN}`,
                'Content-Type': 'application/json',
            },
        },
    );
    if (check(res, { 'evict status 204': (r) => r.status === 204 })) {
        evictSuccess.add(1);
    }
}

export function handleSummary(data) {
    const phase = __ENV.PHASE || 'mixed';
    return {
        [`results-mixed-${phase}.json`]: JSON.stringify(data, null, 2),
        stdout: textSummary(data, { indent: '  ', enableColors: true }),
    };
}
