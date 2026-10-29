# 가까이 (Closer) — Backend

> 내 주변 지도에 흐릿하게 떠 있는 사진을 따라 걸어가면, 가까이 가야만 사진과 한 줄이 열리고 같은 자리에 내 순간을 남기는 위치 기반 서비스.

2026 단디톤 팀 꼬꼬(KkoKko)의 백엔드 저장소입니다. 대구·경북의 "속정"(멀리선 무뚝뚝하지만 다가가야 열리는 정)을 서비스 구조로 옮겼습니다. MVP 범위는 영남대 캠퍼스와 학교 앞입니다.

백엔드는 "흐릿한 지도 → 다가가서 열기 → 남기기" 한 사이클을 **서버 판정으로 보장**합니다. 거리·공개 시각·익명성은 앱이 보낸 값을 믿지 않고 서버가 판단합니다.

## 기술 스택

| 영역 | 사용 | 상태 |
|---|---|---|
| 언어·프레임워크 | Java 17, Spring Boot 3.5.16, Gradle 8.14.6 (Groovy) | 적용 |
| DB | MySQL 8.4, Spring Data JPA | 적용 |
| 인증 | Spring Security + JWT (Access + Refresh), BCrypt | 예정 |
| API 문서 | springdoc-openapi (Swagger UI) | 예정 |
| 이미지 저장 | S3 비공개 버킷 + presigned URL | 예정 |
| AI 연동 | AI 서버(FastAPI)를 REST로 호출 | 예정 |
| 배포 | AWS EC2 + Docker Compose + GitHub Actions, Nginx + HTTPS | 예정 |

Spring Boot 버전을 고른 이유는 [ADR 0001](docs/adr/0001-spring-boot-3.5.md)에 있습니다.

## 로컬 실행

필요한 것: [mise](https://mise.jdx.dev/) (Java 버전 관리), Docker (OrbStack 등)

```bash
# 1. Java 17 설치 (mise.toml 기준)
mise install

# 2. 환경변수 파일 만들기 — 값을 채운다 (로컬 DB_PORT는 3307)
cp .env.example .env

# 3. 로컬 MySQL 띄우기
docker compose up -d

# 4. 실행 / 테스트
./gradlew bootRun
./gradlew test
```

- `.env`는 Docker Compose와 Spring이 같이 읽습니다. Spring은 `application.properties`의 `spring.config.import=optional:file:.env[.properties]`로 읽습니다.
- `.env`는 커밋하지 않습니다. 새 키가 생기면 `.env.example`에 **키 이름만** 추가합니다.
- 테스트(`contextLoads`)도 로컬 MySQL에 연결하므로 3번을 먼저 해야 합니다.

## 프로필

| 프로필 | 용도 | DB | ddl-auto |
|---|---|---|---|
| `local` (기본) | 로컬 개발 | `localhost:${DB_PORT}` (compose) | update |
| `prod` | 개발·운영 서버 | `${DB_HOST}:${DB_PORT}` (환경변수) | update |

운영 서버는 `SPRING_PROFILES_ACTIVE=prod`로 실행합니다.

## 디렉토리

```
src/main/java/com/kkokko/closer
├─ controller   요청·응답만
├─ service      판단 로직
├─ repository   JpaRepository
├─ domain       엔티티
├─ dto          XxxReqDto / XxxResDto
├─ ai           AI 서버 클라이언트
└─ global       config, security, exception, util
```

패키지는 기능이 들어올 때 담당자가 만듭니다. 파일별 담당은 [협업 규칙](docs/CONVENTIONS.md#파일-담당)에 있습니다.

## 개발 일정

| 날짜 | 목표 |
|---|---|
| 10/31 | 서버 열고 기초 작업 (엔티티, 공통 예외, 인증 기반, 개발 서버) |
| 11/3 | 핵심 기능 거의 완성 |
| 11/6 | 기능 완성 + 현장 실테스트 |
| 11/8 | QA |

마일스톤마다 각자 `develop-이름` 브랜치에서 `main`으로 PR을 올리고, 서로 리뷰한 뒤 병합합니다.

## 문서

| 문서 | 내용 |
|---|---|
| [docs/CONVENTIONS.md](docs/CONVENTIONS.md) | 브랜치·커밋·PR·코드 규칙, 파일 담당 |
| [docs/adr/](docs/adr/) | 기술 결정 기록 |

## 팀 (Backend)

| 역할 | 담당 |
|---|---|
| BE-A 차광진 | 계정·사진 남기기·공통 설정 (인증, 공통 예외, Swagger, 업로드, 이미지 저장소, AI 해시태그·얼굴 감지) |
| BE-B 김승윤 | 데이터·서버·지도·열기 (엔티티, 기초 데이터, 개발 서버·배포, 위치 유틸, 온보딩, 지도, 열기, 좋아요, 대표 사진) |
