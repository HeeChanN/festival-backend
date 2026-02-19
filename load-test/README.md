# 메인페이지 캐시 성능 부하 테스트 가이드

## 개요

3단계로 부하 테스트를 진행하여 캐시 도입 효과를 측정합니다.

```
① Baseline (캐시 없음)    → 현재 main 브랜치
② Phase 1 (Redis 캐시)    → feat/phase-1-redis-cache 브랜치
③ Phase 2 (Caffeine+Pub/Sub) → feat/phase-2-caffeine-pubsub 브랜치 (현재 코드)
```

---

## 사전 준비

### 1. k6 설치

```bash
# Linux (EC2)
sudo gpg -k
sudo gpg --no-default-keyring --keyring /usr/share/keyrings/k6-archive-keyring.gpg \
  --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys C5AD17C747E3415A3642D57D77C6C491D6AC1D68
echo "deb [signed-by=/usr/share/keyrings/k6-archive-keyring.gpg] https://dl.k6.io/deb stable main" \
  | sudo tee /etc/apt/sources.list.d/k6.list
sudo apt-get update && sudo apt-get install k6

# Mac
brew install k6

# Windows
winget install k6
```

### 2. EC2 부하 테스트 인스턴스 (권장)

같은 VPC 내 **t3.medium Spot 인스턴스**에서 실행하면 인터넷 레이턴시 없이 순수 서버 성능을 측정할 수 있습니다.

```bash
# EC2에 k6 스크립트 전송
scp -i <keypair.pem> load-test/*.js ec2-user@<테스트인스턴스IP>:~/load-test/
```

### 3. 테스트 데이터 준비

테스트할 `festivalId`에 해당하는 축제 데이터가 DB에 존재해야 합니다.
위젯, 공지, 실종자 데이터가 실제 운영과 비슷한 양으로 세팅되어 있으면 더 정확합니다.

---

## 테스트 절차

### Step 1: Baseline 테스트 (캐시 없음)

현재 `main` 브랜치(캐시 변경 없는 상태)를 배포하고 테스트합니다.

```bash
# main 브랜치 배포 후 실행
k6 run baseline-test.js \
  -e TARGET_HOST=<서버주소:포트> \
  -e FESTIVAL_ID=1 \
  -e PHASE=baseline
```

결과 파일: `results-baseline.json`

---

### Step 2: Phase 1 테스트 (Redis 글로벌 캐시)

Phase 1 브랜치를 만들어 Redis @Cacheable을 적용합니다.

#### Phase 1에서 변경할 파일들

**1) `build.gradle` — 의존성 추가 (Phase 2와 동일)**
```groovy
//cache
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
implementation 'org.springframework.boot:spring-boot-starter-cache'
implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310'
```
> Phase 1에서는 Caffeine 의존성이 필요 없습니다.

**2) `application.yml` — Redis 설정 추가**
```yaml
spring:
  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
      timeout: 3000ms
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 2
```

**3) `RedisCacheConfig.java` — 새로 생성**
```java
package com.halo.eventer.global.config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableCaching
public class RedisCacheConfig implements CachingConfigurer {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);

        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(objectMapper);

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(Duration.ofMinutes(5))
                .prefixCacheNameWith("festimap:");

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new CustomCacheErrorHandler();
    }
}
```

**4) `CustomCacheErrorHandler.java` — 새로 생성**
```java
package com.halo.eventer.global.config;

import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
        log.warn("Cache GET failed for key={}, falling back to DB", key, e);
    }

    @Override
    public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
        log.warn("Cache PUT failed for key={}", key, e);
    }

    @Override
    public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
        log.warn("Cache EVICT failed for key={}", key, e);
    }

    @Override
    public void handleCacheClearError(RuntimeException e, Cache cache) {
        log.warn("Cache CLEAR failed", e);
    }
}
```

**5) `HomeService.java` — @Cacheable 적용**
```java
@Service
@RequiredArgsConstructor
public class HomeService {

    private final NoticeService noticeService;
    private final FestivalRepository festivalRepository;
    private final MissingPersonService missingPersonService;

    @Cacheable(value = "home", key = "#festivalId")
    @Transactional(readOnly = true)
    public HomeDto getMainPage(Long festivalId) {
        Festival festival = getFestival(festivalId);
        return new HomeDto(getBanner(festivalId), festival, LocalDateTime.now(), getMissingPersons(festivalId));
    }

    // ... private 메서드들은 동일
}
```
> Phase 1에서는 `DistributedCacheManager`를 사용하지 않습니다. Spring의 `@Cacheable`이 Redis를 직접 사용합니다.

**6) `HomeCacheService.java` — Spring Cache evict 사용**
```java
@Service
@RequiredArgsConstructor
@Slf4j
public class HomeCacheService {

    private final CacheManager cacheManager;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onHomeCacheEvict(HomeCacheEvictEvent event) {
        log.debug("Evicting home cache for festivalId: {}", event.getFestivalId());
        Cache homeCache = cacheManager.getCache("home");
        if (homeCache != null) {
            homeCache.evict(event.getFestivalId());
        }
    }
}
```

**7) 위젯/공지/실종자 서비스** — Phase 2와 동일하게 `HomeCacheEvictEvent` 발행
(이 부분은 Phase 2 코드와 동일합니다)

#### Phase 1 배포 & 테스트

```bash
# ElastiCache 또는 로컬 Redis가 실행 중인 상태에서 배포

# 로컬 테스트 시 Docker Redis 실행
docker run -d --name redis-test -p 6379:6379 redis:7-alpine

# Phase 1 부하 테스트
k6 run baseline-test.js \
  -e TARGET_HOST=<서버주소:포트> \
  -e FESTIVAL_ID=1 \
  -e PHASE=phase1
```

결과 파일: `results-phase1.json`

---

### Step 3: Phase 2 테스트 (Caffeine + Redis Pub/Sub)

현재 구현된 코드(Caffeine + Pub/Sub)를 배포하고 테스트합니다.

```bash
# Phase 2 코드 배포 후 실행
k6 run baseline-test.js \
  -e TARGET_HOST=<서버주소:포트> \
  -e FESTIVAL_ID=1 \
  -e PHASE=phase2
```

결과 파일: `results-phase2.json`

---

### Step 4: 추가 시나리오 테스트 (선택)

각 Phase에서 추가 시나리오도 실행할 수 있습니다.

```bash
# 스파이크 테스트
k6 run spike-test.js \
  -e TARGET_HOST=<서버주소:포트> \
  -e FESTIVAL_ID=1 \
  -e PHASE=<baseline|phase1|phase2>

# 읽기/쓰기 혼합 테스트 (JWT 토큰 필요)
k6 run mixed-test.js \
  -e TARGET_HOST=<서버주소:포트> \
  -e FESTIVAL_ID=1 \
  -e AUTH_TOKEN=<JWT토큰> \
  -e PHASE=<baseline|phase1|phase2>
```

---

### Step 5: 결과 비교

3개의 결과 파일이 모두 있으면 비교 스크립트를 실행합니다.

```bash
cd load-test
node compare-results.js
```

출력 예시:
```
| 지표 | No Cache | Phase 1 (Redis) | Phase 2 (Caffeine+Pub/Sub) |
| --- | --- | --- | --- |
| p50 (ms) | 45.23 | 12.34 | 1.56 |
| p95 (ms) | 120.56 | 35.78 | 3.21 |
| p99 (ms) | 250.89 | 78.45 | 8.90 |
| Throughput (req/s) | 450.00 | 1200.00 | 3500.00 |
| Error Rate (%) | 0.00 | 0.00 | 0.00 |

--- Phase 2 vs Baseline ---
p95 개선: 97.3%
Throughput 개선: +677.8%
```

---

## 핵심 차이 요약

| 항목 | Phase 1 (Redis) | Phase 2 (Caffeine+Pub/Sub) |
|------|-----------------|---------------------------|
| 캐시 위치 | Redis (네트워크) | JVM 메모리 (인프로세스) |
| 읽기 경로 | App → Redis → (miss시) DB | App → Caffeine → (miss시) DB |
| 네트워크 비용 | 매 요청 Redis RTT (~0.5ms) | 0ms |
| 역직렬화 | 매 요청 JSON 역직렬화 | 없음 (객체 참조) |
| 무효화 | 로컬 CacheManager evict | Redis Pub/Sub 브로드캐스트 |
| TTL | 5분 | 1시간 (+ Pub/Sub 실시간 무효화) |
| 다중 인스턴스 일관성 | Redis가 single source | Pub/Sub으로 동기화 (결과적 일관성) |

---

## Git 브랜치 운영

```bash
# 1. 현재 변경사항을 Phase 2 브랜치로 이동
git stash
git checkout -b feat/phase-2-caffeine-pubsub
git stash pop
git add -A && git commit -m "feat: Caffeine 로컬 캐시 + Redis Pub/Sub 도입"

# 2. Phase 1 브랜치 생성 (main에서)
git checkout main
git checkout -b feat/phase-1-redis-cache
# → Phase 1 코드 적용 (위 가이드 참고)
git add -A && git commit -m "feat: Redis 글로벌 캐시 도입"

# 3. 테스트 순서
git checkout main                        # Baseline 테스트
git checkout feat/phase-1-redis-cache    # Phase 1 테스트
git checkout feat/phase-2-caffeine-pubsub # Phase 2 테스트
```

---

## 로컬에서 간편 테스트

AWS 환경 없이 로컬에서도 간편하게 비교 테스트할 수 있습니다.

```bash
# 1. 로컬 Redis 실행
docker run -d --name redis-local -p 6379:6379 redis:7-alpine

# 2. 서버 시작 (각 브랜치별)
./gradlew bootRun

# 3. k6 부하 테스트 (VU 수를 줄여서)
k6 run baseline-test.js \
  -e TARGET_HOST=localhost:8080 \
  -e FESTIVAL_ID=1 \
  -e PHASE=<baseline|phase1|phase2>
```

> 로컬 테스트는 절대적인 수치보다 **상대 비교(before/after)**에 의미를 둡니다.
> 블로그에서는 "p95 레이턴시가 X% 감소, 처리량이 Y배 증가" 같은 상대 개선률을 강조하세요.
