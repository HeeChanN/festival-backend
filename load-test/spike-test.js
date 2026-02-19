import http from 'k6/http';
import { check, sleep } from 'k6';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.2/index.js';

/**
 * 시나리오 2: 스파이크 테스트 (Cold Start)
 *
 * 캐시가 비어있는 상태에서 갑작스런 트래픽 유입을 시뮬레이션.
 * 캐시 스탬피드(Cache Stampede) 발생 여부를 확인.
 *
 * 사용법:
 *   k6 run spike-test.js -e TARGET_HOST=<서버주소> -e FESTIVAL_ID=1
 */

const TARGET_HOST = __ENV.TARGET_HOST || 'localhost:8080';
const FESTIVAL_ID = __ENV.FESTIVAL_ID || '1';

export const options = {
    stages: [
        { duration: '5s',  target: 100 },  // 0→100 VU 급증
        { duration: '30s', target: 100 },  // 100 VU 유지
        { duration: '5s',  target: 0 },    // 램프다운
    ],
    thresholds: {
        http_req_duration: ['p(99)<1000'],  // p99 < 1초
        http_req_failed: ['rate<0.05'],     // 에러율 < 5%
    },
};

export default function () {
    const res = http.get(`http://${TARGET_HOST}/home/${FESTIVAL_ID}`);

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    sleep(0.05); // 50ms 간격 (더 공격적인 부하)
}

export function handleSummary(data) {
    const phase = __ENV.PHASE || 'baseline';
    return {
        [`results-spike-${phase}.json`]: JSON.stringify(data, null, 2),
        stdout: textSummary(data, { indent: '  ', enableColors: true }),
    };
}
