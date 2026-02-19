/**
 * 부하 테스트 결과 비교 스크립트
 *
 * 3단계(Baseline, Phase 1, Phase 2)의 k6 결과 JSON을 읽어
 * 블로그용 비교 테이블을 생성합니다.
 *
 * 사용법:
 *   node compare-results.js
 *
 * 필요 파일:
 *   results-baseline.json
 *   results-phase1.json
 *   results-phase2.json
 */

const fs = require('fs');

function loadResults(filename) {
    try {
        const data = JSON.parse(fs.readFileSync(filename, 'utf8'));
        const metrics = data.metrics;
        return {
            p50: metrics.http_req_duration.values.med?.toFixed(2) || 'N/A',
            p95: metrics.http_req_duration.values['p(95)']?.toFixed(2) || 'N/A',
            p99: metrics.http_req_duration.values['p(99)']?.toFixed(2) || 'N/A',
            avg: metrics.http_req_duration.values.avg?.toFixed(2) || 'N/A',
            min: metrics.http_req_duration.values.min?.toFixed(2) || 'N/A',
            max: metrics.http_req_duration.values.max?.toFixed(2) || 'N/A',
            throughput: (metrics.http_reqs.values.rate || 0).toFixed(2),
            totalRequests: metrics.http_reqs.values.count || 0,
            errorRate: ((metrics.http_req_failed?.values?.rate || 0) * 100).toFixed(2),
        };
    } catch (e) {
        return null;
    }
}

const baseline = loadResults('results-baseline.json');
const phase1 = loadResults('results-phase1.json');
const phase2 = loadResults('results-phase2.json');

console.log('\n====================================================');
console.log('         부하 테스트 결과 비교 (블로그용)');
console.log('====================================================\n');

const header = ['지표', 'No Cache', 'Phase 1 (Redis)', 'Phase 2 (Caffeine+Pub/Sub)'];
const rows = [
    ['p50 (ms)', baseline?.p50, phase1?.p50, phase2?.p50],
    ['p95 (ms)', baseline?.p95, phase1?.p95, phase2?.p95],
    ['p99 (ms)', baseline?.p99, phase1?.p99, phase2?.p99],
    ['avg (ms)', baseline?.avg, phase1?.avg, phase2?.avg],
    ['max (ms)', baseline?.max, phase1?.max, phase2?.max],
    ['Throughput (req/s)', baseline?.throughput, phase1?.throughput, phase2?.throughput],
    ['Total Requests', baseline?.totalRequests, phase1?.totalRequests, phase2?.totalRequests],
    ['Error Rate (%)', baseline?.errorRate, phase1?.errorRate, phase2?.errorRate],
];

// Markdown 테이블 출력
console.log(`| ${header.join(' | ')} |`);
console.log(`| ${header.map(() => '---').join(' | ')} |`);
rows.forEach(row => {
    console.log(`| ${row.map(v => v ?? '-').join(' | ')} |`);
});

// 개선율 계산
if (baseline && phase1) {
    const p95Improvement1 = ((1 - parseFloat(phase1.p95) / parseFloat(baseline.p95)) * 100).toFixed(1);
    const throughputImprovement1 = ((parseFloat(phase1.throughput) / parseFloat(baseline.throughput) - 1) * 100).toFixed(1);
    console.log(`\n--- Phase 1 vs Baseline ---`);
    console.log(`p95 개선: ${p95Improvement1}%`);
    console.log(`Throughput 개선: +${throughputImprovement1}%`);
}

if (baseline && phase2) {
    const p95Improvement2 = ((1 - parseFloat(phase2.p95) / parseFloat(baseline.p95)) * 100).toFixed(1);
    const throughputImprovement2 = ((parseFloat(phase2.throughput) / parseFloat(baseline.throughput) - 1) * 100).toFixed(1);
    console.log(`\n--- Phase 2 vs Baseline ---`);
    console.log(`p95 개선: ${p95Improvement2}%`);
    console.log(`Throughput 개선: +${throughputImprovement2}%`);
}

if (phase1 && phase2) {
    const p95Diff = ((1 - parseFloat(phase2.p95) / parseFloat(phase1.p95)) * 100).toFixed(1);
    console.log(`\n--- Phase 2 vs Phase 1 ---`);
    console.log(`p95 추가 개선: ${p95Diff}%`);
}

console.log('\n');
