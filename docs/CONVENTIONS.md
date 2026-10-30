# 협업 규칙

멋사 14기 BE 강의에서 배운 방식을 기준으로 합니다. 바꿀 때는 이 문서를 먼저 고치고 PR로 합의합니다.

## 브랜치

```
main                 배포·제출 기준. 직접 push 금지
├─ develop-seungyoon  BE-B 김승윤 작업 브랜치
└─ develop-gwangjin   BE-A 차광진 작업 브랜치
```

- 각자 자기 `develop-이름` 브랜치에서만 작업합니다.
- 마일스톤(10/31, 11/3, 11/6, 11/8)마다 `develop-이름` → `main` PR을 올립니다.
- 상대 1명이 리뷰하고 **승인(Approve)한 뒤에만** 병합합니다.
- 병합 방식은 **Create a merge commit**입니다. 기능별 커밋 기록이 main에 그대로 남도록 squash는 쓰지 않습니다.
- 상대 PR이 main에 병합되면 내 브랜치에 바로 반영합니다. 오래 벌어지면 충돌이 커지기 때문입니다.

  ```bash
  git fetch origin
  git merge origin/main
  ```

## 커밋

형식은 `타입: 요약`이고 한국어로 씁니다.

| 타입 | 쓰는 경우 |
|---|---|
| `feat` | 기능 추가 |
| `fix` | 버그 수정 |
| `refactor` | 동작 변화 없는 구조 개선 |
| `test` | 테스트 추가·수정 |
| `docs` | 문서 |
| `chore` | 빌드·설정·의존성 |

- 커밋 하나에는 변경 하나만 담습니다. 예를 들어 엔티티 추가와 API 추가는 나눠서 커밋합니다.
- 관련 이슈가 있으면 본문에 `#번호`를 적습니다.

## 이슈

- 기능 하나가 이슈 하나입니다. 이슈 템플릿(기능 / 버그)을 씁니다.
- 이슈 제목은 `[BE-A]` 또는 `[BE-B]` 머리말을 붙이고 기능 이름을 적습니다. 예: `[BE-B] 엔티티 8개와 Repository`

## PR

- PR 템플릿을 채웁니다: 작업 내용, 관련 이슈, 바뀐 API, 테스트 방법.
- 내가 담당하지 않은 파일을 고쳤다면 그 파일 담당자를 반드시 리뷰어로 넣습니다.
- API가 바뀌면 Swagger를 갱신하고 팀 단톡에 공지합니다.

## 문서

문서는 코드와 같은 PR에 같이 올립니다. 코드만 바뀌고 문서가 그대로면 리뷰에서 반려합니다.

| 시점 | 해야 할 것 | 문서 |
|---|---|---|
| **작업 전** | 되돌리기 비싼 결정(라이브러리, 인프라, 저장 방식, PRD와 다르게 가는 것)은 결정 문서를 먼저 PR로 올려 합의한 뒤 구현 | [docs/adr/](adr/) |
| 작업 PR과 함께 | 엔티티·테이블·인덱스 변경 | [ERD.md](ERD.md) |
| | API 추가·변경 | Swagger 설명·예시, PR "바뀐 API" 표, 팀 단톡 공지 |
| | 구성 요소·핵심 규칙 변경 | [ARCHITECTURE.md](ARCHITECTURE.md) |
| | 서버·배포 절차 변경 | [DEPLOY.md](DEPLOY.md) |
| | 환경변수·설정값 추가 | `.env.example`(키 이름만), README |
| 문제가 생겼을 때 | 해결 전이라도 바로 기록. 터질 것 같은 부분은 "지켜볼 것"에 먼저 | [TROUBLESHOOTING.md](TROUBLESHOOTING.md) |
| 마일스톤 PR | README 기술 스택 상태, 아래 "서로 넘겨줄 것" 상태 갱신 | README, 이 문서 |
| 대회 후 | 회고 | 추가 예정 |

- 문서에도 비밀값(IP, 키, 비밀번호, 계정 정보)을 적지 않습니다.
- 문서는 사실만 적습니다. 확인하지 못한 것은 "추정", 정하지 않은 것은 "정할 것"으로 표시합니다.

## 코드 규칙

| 항목 | 규칙 |
|---|---|
| 패키지 | 계층별: `controller` / `service` / `repository` / `domain` / `dto` + `global` (config, security, exception, util) |
| 계층 | Controller는 요청·응답만, 판단은 Service, DB 접근은 Repository |
| DTO | 요청·응답 분리: `XxxReqDto`, `XxxResDto` + `static from(엔티티)`. 엔티티를 직접 반환하지 않는다 |
| 엔티티 | `@Builder`로 생성, `@ManyToOne(fetch = LAZY)` + `@JoinColumn`, 양방향이면 편의 메서드 |
| 주입 | `@RequiredArgsConstructor` 생성자 주입 |
| 검증 | ReqDto에 `@NotBlank` 등 + 컨트롤러 `@Valid` |
| 응답 | `ResponseEntity`로 상태코드 명시 |
| 예외 | `ErrorCode` enum(status, message) → `CustomException` → `@RestControllerAdvice` `GlobalExceptionHandler` → `ErrorDto` |
| 에러 응답 | `{status, code, message}`. 같은 400이라도 의미를 구분하려고 `code`(enum 이름)를 추가한다 |
| Repository | `JpaRepository` 상속, 메서드 이름 쿼리 우선 |
| API 경로 | `/api/v1/...` |
| 위치 값 | 항상 `lat`, `lng`, `accuracy`(m) |
| 비밀값 | `.env`는 커밋 금지, 배포 정보는 GitHub Secrets |
| 설정값 | 거리·지연 시간 같은 수치는 코드에 박지 않고 `application.properties`로 뺀다 (현장 튜닝용) |
| 테스트 | Swagger로 돌려본 건 테스트가 아니다. 거리 단계·지연 공개·위치 스냅은 단위 테스트를 둔다 |

### ErrorCode 작성 규칙

두 사람이 같은 `ErrorCode` 파일에 추가하므로, 충돌이 나지 않게 도메인별 주석 블록(`// auth`, `// post`, `// map`, `// open` …) 아래에만 씁니다.

### 공개 조회 규칙

남의 사진을 조회하는 모든 쿼리는 `status = VISIBLE AND publish_at <= now()` 조건을 겁니다. 본인 조회는 이 조건을 걸지 않습니다. 이 조건이 들어간 Repository 메서드를 하나 두고 같이 씁니다.

## 파일 담당

담당이 아닌 파일을 고칠 때는 PR 리뷰어로 담당자를 넣습니다.

| 영역 | BE-A 차광진 | BE-B 김승윤 |
|---|---|---|
| 공통 | 공통 예외(ErrorCode·Handler), SecurityConfig·CORS, JWT, SwaggerConfig, S3Config | 엔티티 전부, Repository 전부, data.sql, GeoUtils |
| controller | AuthController, UserController, PostController | MapController, OpenController, 온보딩·태그·질문 컨트롤러, RecommendController |
| service | AuthService, UserService, PostService, ImageService, LocationBlurService | MapService, OpenService, LikeService, TasteService, 온보딩 서비스, AchievementService |
| AI 연동 | AiClient (공통 RestClient, 해시태그·얼굴) | AiClient의 경로 추천 메서드 |
| 인프라 | 이미지 저장소 | EC2, MySQL, Docker Compose, GitHub Actions, 도메인·HTTPS |
| 프로젝트 기반 | build.gradle, application*.properties, .env.example, README, 이 문서 — 둘이 공동으로 관리하고 바꿀 때 PR로 알린다 | |

- 온보딩·태그·질문 API(`PUT /users/me/tags`, `GET /tags`, `GET /questions/random`)는 최종 분담표 기준으로 BE-B 담당입니다.
- `/api/v1/posts/...`는 둘이 나눠 쓰지만 컨트롤러 파일이 다릅니다(PostController는 A, OpenController는 B). 메서드 경로만 겹치지 않으면 됩니다.

## 서로 넘겨줄 것

받는 사람이 이 지점에서 막히므로 가장 먼저 처리합니다. 급하면 메서드 시그니처만 먼저 병합합니다.

| 주는 사람 | 무엇 | 받는 사람·용도 | 상태 |
|---|---|---|---|
| A | ErrorCode, CustomException, GlobalExceptionHandler, ErrorDto | B: 모든 예외 처리 | 완료 (#2) |
| B | 엔티티 8개 + Repository | A: 회원가입·업로드 | PR #6 |
| A | JWT 필터 + 컨트롤러에서 로그인 userId를 꺼내는 방법: 파라미터에 `@AuthenticationPrincipal Long userId` | B: 열기·좋아요 | 완료 (Security PR) |
| B | data.sql: 태그 사전, zone 좌표 | A: 온보딩·태그 후보 테스트 | 태그·좌표 받는 중 |
| B | 개발 서버(EC2) + Swagger URL | 프론트 전체 | 진행 중 |
| B | `GeoUtils.distance()`, `GeoUtils.snap()`(→ `LatLng`), `BlurStage` | A: 업로드 위치 흐림, `ImageService.urlFor` | PR #6 |
| A | `ImageService.urlFor(post, stage)` — 단계별 이미지 URL | B: 지도·열기 응답 | |
| B | `MapService.findZone(lat, lng)` — 가장 가까운 zone | A: 업로드 zone 배정 | PR #6 |
| A | `AiClient` (RestClient 빈 + 타임아웃) | B: 경로 추천 메서드 추가 | |

## 매일 싱크

10분 동안 어제 한 것, 막힌 것, API 변경을 공유합니다. 하루 이상 막히면 바로 말해서 다른 사람이 가져갑니다.
