# 지도 기반 축제 정보 제공 서비스 (Waba) 

담당 역할
- 백엔드 API 서버 개발 및 성능 개선
- 클라우드 인프라 구축


## 백엔드 개발 문제 해결 및 개선

<br>

### 메인페이지 조회 성능 개선

2024년 10월 경희대 축제 운영 중에 사이트 이용자 증가로 웹사이트 메인 페이지의 사용자 응답 속도가 느려지는 현상을 발견하였습니다. 

원인을 찾기 위해 메인 페이지 조회 로직의 쿼리 로그를 점검한 결과, 페이지 내의 각 요소를 조회할 때 **개별 테이블을 조회해서 불필요한 쿼리**를 생성하고 있었으며, JPA ORM의 **불필요한 Outer Join**과 지연 로딩(Lazy Loading)으로 인한 **추가적인 쿼리 호출**이 발생하고 있었습니다.

문제를 해결하기 위해 먼저, 비슷한 기능들을 가진 4가지 위젯들을 **단일 테이블 상속 전략으로 하나의 테이블**에서 통합 관리하도록 변경하였으며, 외래키 기반 조회 시 **직접 쿼리를 작성**하여 Outer Join을 where절로 변경했으며, **페치 조인**을 통해 지연로딩에 의한 추가 쿼리를 최소화했습니다.

이후 Locust로 동일한 환경에서 테스트 한 결과 반응 속도가 대폭 감소하였습니다.
(**1000명 동시 접속자 기준 반응속도 1300ms → 210ms)**
    
최적화 결과는 만족스러웠으나, 한 번 저장된 데이터를 반복적으로 조회하는 서비스 특성을 고려하면 **인메모리 캐시**를 도입하는 것이 더욱 효과적으로 데이터베이스 접근 횟수를 줄일 수 있는 방안이라고 생각했습니다. 

<br>

### 파일 업로드 기능 서버 부하 감소

Spring에서 **MultipartFile**을 사용해 파일을 업로드하는 과정에서, 작은 규모의 서버에서 다수의 사용자가 용량이 큰 파일을 업로드할 경우 서버의 응답 시간이 길어지는 문제가 발생했습니다. 특히, 서버에서 AWS S3에 파일을 업로드하는 과정이 지연되어 성능 저하가 두드러졌습니다.

문제의 원인은 다음과 같았습니다.

MultipartFile을 사용한 업로드에서는 파일이 서버에 도착한 후, 해당 파일이 서버에 임시 저장된 뒤 다시 AWS S3로 전송됩니다.
이 과정에서 용량이 큰 파일을 처리할 경우 서버의 I/O 부하가 증가하고, 메모리 사용량이 급격히 늘어나면서 서버의 응답 시간이 길어지는 문제가 발생했습니다.
또한, S3로 파일을 전송하는 과정에서 파일 크기가 클수록 네트워크 I/O 지연이 심화되었고, 동시에 여러 사용자가 업로드를 시도하면 서버 성능이 더욱 저하되었습니다.

이 문제를 해결하기 위해 서버를 Proxy로 거치지 않고 바로 S3에 업로드할 수 있는 Pre-signed URL 방식을 도입했습니다. 이 방식에서는 파일이 서버를 경유하지 않으므로, 서버 I/O 부하가 크게 감소하고 응답 시간이 단축됩니다.

결과적으로 동일한 환경에서 5명이 각각 50MB를 업로드한 테스트에서 서버 자체의 응답시간이 250MB 크기 기준 23000ms → 48ms로 감소할 수 있었습니다.

하지만, 유저의 응답시간의 대상이 API 서버에서 S3로 바뀐점에서 개선할 사항은 있어보입니다. S3 Multipart Upload는 이런 유저의 직접적인 반응 시간을 감소시킬 수 있는 방법이라고 생각합니다.
현재 프로젝트에서는 실제로 한 유저가 50MB파일을 한번에 업로드하는 서비스 로직이 존재하지 않기 때문에 실제 유저의 s3 업로드 응답시간은 길지 않을 것이라 판단하여 최종적으로 Pre-signed URL 방식까지만 도입하였습니다.

<br>

### 문의사항 페이징 조회 개선

<br>

서비스에서 사용자가 글을 등록하는 기능 중 하나인 문의사항은 다른 기능에 비해 데이터가 빠르게 누적되었습니다. 따라서 해당 테이블에 대한 페이징 조회 기능을 점검하였는데,
Pageable 객체를 이용한 Offset 방식을 사용할 때 1,000만 개의 목업 데이터를 기준으로 테스트를 진행한 결과 총 49초라는 매우 긴 시간이 소요되었습니다. 이러한 응답 시간은 사용자 경험 측면에서 수용하기 어려운 수준이었으며, 성능 개선이 필요했습니다.

원인은 Offset 방식에서는 페이지 번호가 커질수록 건너뛰는(Offset) 데이터가 많아지며, 이를 찾기 위한 테이블 풀 스캔 작업이 증가하는 것이었습니다. 이 과정에서 데이터가 많아질수록 조회 시간이 기하급수적으로 늘어나는 문제가 발생했습니다.

이 문제를 해결하기 위해 인덱스 기반 Next Key 방식을 도입했습니다. Next Key 방식은 특정 인덱스를 기준으로 이후 데이터를 조회하는 방식으로, Offset 방식과 달리 건너뛰는 데이터 없이 즉시 다음 데이터를 조회할 수 있어 성능이 크게 향상됩니다. 또한, 마지막 페이지에서는 다음 페이지가 없다는 정보를 제공하도록 구현하여, 불필요한 추가 요청 횟수를 1회 줄이는 최적화를 추가했습니다.

결과적으로 1,000만 개의 목업 데이터 기준으로 기존 49초 → 0.015초로 대폭 단축되었습니다.

`추가 고민 및 향후 개선 방향` <br>

백오피스에서는 무한 스크롤 방식보다는 페이지 번호 기반의 전통적인 페이징 UI가 사용자 친화적일 수 있습니다. 이 경우 Next Key 방식을 사용할 수 없는 구조이므로, 현재는 Next Key 방식과 Offset 방식의 혼합 구조로 구현했습니다. 향후에는 백오피스 환경에서도 효율적인 Offset 페이징 최적화 기법을 추가적으로 검토할 계획입니다.



<br>

### JWT 기반 헤더기반 인증 로직 구현

인증 방식을 결정하는 과정에서 **세션**과 JWT 중 JWT를 선택하게 된 주요 이유는 다음과 같습니다.

세션 기반 인증에서는 매 요청마다 클라이언트로부터 전달받은 SessionId를 캐시나 데이터베이스에 저장된 유효한 세션 정보와 대조하여 검증해야 합니다. 이 과정에서 매번 네트워크 I/O가 발생하게 되며, 이는 성능 저하로 이어질 수 있습니다.

반면, JWT는 토큰 자체에 사용자 정보와 유효성 정보가 담겨 있어, 서버에서는 별도의 데이터베이스 조회 없이 Secret Key를 통해 자체적으로 검증을 수행할 수 있습니다. 이처럼 네트워크 I/O 없이 인증을 처리할 수 있다는 장점 때문에 JWT 기반 인증 방식을 선택하게 되었습니다.

JWT 인증 방식을 채택한 이후, 구체적인 구현 방식으로 헤더 기반과 쿠키 기반 중 헤더 방식을 선택하게 되었습니다.

헤더 방식을 선택한 주요 이유는 다음과 같습니다.
쿠키 기반 인증은 동일 도메인에서만 자동으로 쿠키를 전송하는 특성상, 다양한 도메인을 사용하는 구조에서는 한계가 있습니다.
서비스 운영 과정의 초기 검증 단계에서는 서브도메인 기반이 아닌, 클라이언트 환경 배포 및 축제마다 다른 도메인을 통해 배포하는 방식으로 진행하기로 결정이 됬고 이러한 구조에서는 쿠키 기반 인증이 원활하게 동작하지 않기 때문에 헤더 기반 인증이 적합하다고 판단했습니다.

또한, 쿠키를 사용할 경우, 팀원들이 Admin 역할을 맡더라도 CSRF 공격에 대비해야 하는 보안 조치가 필요합니다. 반면, 헤더 기반 방식은 브라우저가 자동으로 토큰을 전송하지 않으므로, CSRF 공격의 위험이 원천적으로 차단됩니다. 또한, 관리자가 팀원으로 한정된 상황에서는 게시글 작성과 같은 기능에서 XSS 공격에 대한 대응을 보다 완화된 기준으로 설정할 수 있다는 점도 고려되었습니다.


결과적으로, 기획적인 요구사항과 보안 측면을 종합적으로 고려한 결과, 보다 유연한 도메인 구조를 지원하고 CSRF 공격 방어에 유리한 헤더 기반 JWT 인증 방식을 선택하게 되었습니다.


<br>

## 클라우드 인프라 환경 구축시 고려했던 사항

<br>

### 가용성 문제 개선

AWS 클라우드 환경에서 단일 서버(t4g.micro)로 서비스를 운영하고 있었으나, 트래픽이 급격하게 증가하는 상황에 대한 대비가 미흡하였습니다. 이로 인해 많은 사용자가 동시에 접속하는 상황에서 서버가 트래픽을 제대로 감당하지 못할 위험이 있었습니다.

근본적인 원인은 단일 서버의 하드웨어 자원이 제한적이라는 것이었습니다. 서버 스펙이 2vCPU, 1GB 메모리에 불과하여 다수의 동시 접속자가 몰리는 환경에서 증가하는 부하를 안정적으로 처리하는 데 어려움이 있었습니다. 이는 장기적으로 서비스 품질에 큰 영향을 미칠 수밖에 없는 구조였습니다.

해결방안으로는 두가지 방식을 고려했습니다.

1. Scale Up (서버 사양 증대)<br>
가장 직관적인 방법은 서버 자체의 사양을 높이는 것이었습니다. 하지만 AWS 환경에서 단순히 CPU 자원을 늘릴 경우 비용이 기하급수적으로 증가하여(예를 들어 CPU를 두 배 늘릴 때 비용이 최대 16배까지 증가) 비효율적인 선택으로 판단했습니다.

2. Scale Out (서버 수를 늘리는 방식)<br>
비용 효율성을 극대화하기 위해 Scale Out 방식을 선택했습니다. 특히 Auto Scaling 기능을 도입해 트래픽이 몰리는 특정 시점에만 서버 개수가 자동으로 증가하도록 구성했습니다. 평상시에는 적은 비용을 유지하면서, 트래픽 증가 시 탄력적으로 대응이 가능하도록 설계했습니다.

이러한 Scale Out 방식 및 Auto Scaling을 기존 단일 서버 환경에서 500명의 동시 접속자까지 감당할 수 있던 서비스가 1000명의 동시 접속자까지 안정적으로 처리할 수 있도록 개선되었습니다.

다만, 동시 접속자 수가 1000명 이상으로 증가할 경우 DB의 처리 능력이 병목 현상으로 작용하여 성능을 보장하기 어렵습니다. 이 문제는 추후 DB 서버의 자원을 추가로 확장하는 형태로 대응할 계획입니다. 



<br>

### 보안 문제 개선


초기 클라우드 인프라 구축 당시 AWS에서 제공하는 기본 인프라를 그대로 사용했기 때문에, 모든 서버가 퍼블릭 네트워크에 노출되는 상황이 발생했습니다. 특히 데이터베이스 서버나 API 서버와 같은 민감한 자원들이 인터넷상에서 직접 접근 가능한 환경이 되어 외부 공격에 취약했습니다. 보안그룹만으로 접근을 통제하는 방식으로는 외부 침입을 효과적으로 차단하기 어려웠으며, 이로 인해 잠재적인 보안 위협에 노출될 수밖에 없었습니다.

이러한 문제는 기본 제공되는 AWS 인프라가 퍼블릭 서브넷으로만 구성되어 있기 때문에 발생한 것입니다. 즉, 클라우드 환경에서 별도의 사설 네트워크 영역을 생성하지 않은 채, 모든 자원을 공용 인터넷에서 접근 가능한 상태로 둔 것이 주요 원인이었습니다. 네트워크 수준에서의 철저한 접근 제한 없이 오직 보안그룹만을 신뢰한 것이 근본적인 한계였습니다.

이를 해결하기 위해 VPC를 도입하고 네트워크를 체계적으로 분리하였습니다. VPC 내에서 Pulbic 서브넷과 Private 서브넷을 구분하여 데이터베이스 서버 및 주요 API 서버는 인터넷 접근이 제한된 Private 서브넷에 배치하였습니다. Public 서브넷에는 외부에서의 접근을 제한적으로 허용하는 Load Balancer와 Bastion Server를 구성해 단일 진입점으로 설정했습니다. 이를 통해 외부에서 직접 내부 인프라에 접근하지 못하도록 차단하고, 필요한 접근은 미리 정의된 보안 규칙에 따라 제한적으로만 허용하도록 했습니다.

이러한 VPC 네트워크 분리 및 보안 아키텍처를 적용한 결과, Bastion Server와 Load Balancer의 진입점 통합으로 인해 인프라 관리의 편의성과 보안성 모두 증가했으며, 향후 발생 가능한 침해 시도를 보다 쉽게 탐지하고 대응할 수 있는 환경을 마련하게 되었습니다.

이상 탐지 후 개발자에게 Alert을 보내는 시스템을 구축하지 못했습니다. 애플리케이션 서버의 로깅을 바탕으로 비정상적 행위 감지 및 Alert 하는 시스템을 구축하여 사후 분석 및 대응책을 마련할 수 있는 시스템을 구축해야한다고 생각합니다.

<br>

### 비용 문제 개선


초기 클라우드 인프라는 주로 온디맨드 EC2 인스턴스를 활용했으며, 특히 NAT Gateway의 높은 비용으로 인해 예상보다 큰 운영 비용이 발생했습니다.

온디맨드 인스턴스는 유연성이 뛰어나지만 장기적으로 사용할 경우 비용이 크게 증가하는 구조입니다. 또한, NAT Gateway는 데이터 전송량에 따라 상당히 높은 비용이 발생해 지속적인 비용 증가의 주요 원인이 되었습니다.

이 문제를 해결하기 위해 Bastion 서버를 저렴한 Spot 인스턴스로 교체하여 약 90%의 비용을 절감했으며, 장기 운영이 필요한 서버에는 절감형 플랜을 도입해 약 40%의 비용을 추가로 절감했습니다. 또한, NAT Gateway는 AWS의 관리형 서비스 대신 비용이 상대적으로 저렴한 자체 관리형 NAT 인스턴스(t3.nano)로 교체하여 데이터 전송 비용을 크게 절감했습니다.

```markdown
그 결과 한달 비용이 다음과 같이 줄일 수 있었습니다.
스팟 인스턴스 도입 : $0.72 -> $0.12
절감형 플랜 도입 : $35 -> $21
Nat 인스턴스 도입 : $52 -> $4.68 (t3.nano 기준)
```


## 인프라 구조

![image](https://github.com/user-attachments/assets/77a48088-430e-467d-b456-01966af77abf)


<br>

### 무중단 배포 구조

![image](https://github.com/user-attachments/assets/a271b95b-1832-4926-bcf1-fbca8c50a23e)



<br>

### 모니터링 환경

![image](https://github.com/user-attachments/assets/6de99eb7-3cf3-4d8d-b26e-3e32c520fdf7)


