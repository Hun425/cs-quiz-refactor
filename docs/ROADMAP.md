# Project Roadmap

## Objective
학습 목적과 실사용 목적을 동시에 만족하는 CS Quiz 플랫폼을 단계적으로 구축한다.

## Ground Rules
- Main branch is always stable.
- One PR = one bounded context + one layer.
- v1 is synchronous application-call based.
- v2 keeps event/outbox extension points ready.

## Milestones

### M0 - Foundation (Setup)
- Goal: 6모듈 골격, 빌드/실행/기본 품질 게이트 확보
- Done when:
  - `./gradlew build` 통과
  - 6 modules (`proto/domain/application/infrastructure/presentation/bootstrap`) 연결
  - 로컬 실행 진입점 동작(헬스체크)

### M1 - Identity End-to-End
- Goal: Google OAuth 기반 회원 등록/조회/닉네임 변경
- Done when:
  - Domain/Application/Infrastructure/Presentation 구현 완료
  - DB schema + migration 반영
  - gRPC error mapping 완료

### M2 - Quiz End-to-End
- Goal: 카테고리/퀴즈 생성 및 조회
- Done when:
  - 문제 유형/난이도/선택지 규칙 도메인에서 검증
  - 카테고리/퀴즈 CRUD 핵심 흐름 동작

### M3 - Session End-to-End
- Goal: 세션 시작(카테고리 nullable), 답안 제출, 완료
- Done when:
  - 채점 서비스 연동
  - 상태 전이 규칙/중복 제출 방지
  - 결과 집계 응답 일관성

### M4 - Ranking End-to-End
- Goal: 전체/카테고리 + 주간/월간/누적 랭킹
- Done when:
  - 동점 규칙(먼저 달성 우선) 구현
  - `rankings/rank_entries` 모델 반영
  - 조회 API 및 인덱스 점검

### M5 - Hardening
- Goal: 운영 품질 강화
- Done when:
  - 관측성(로그/메트릭/트레이싱) 최소셋
  - 테스트 전략 보강(통합/계약)
  - 이벤트 전환 포인트 명시 완료

## PR Backlog (Initial)

### Track A: Setup
1. PR-A1: Gradle 6모듈 생성 + 공통 빌드 설정
2. PR-A2: proto 모듈 + gRPC codegen 파이프라인
3. PR-A3: bootstrap + Armeria 서버 기동 + health endpoint
4. PR-A4: jOOQ/R2DBC 연결 + 로컬 DB 컨테이너 구성

### Track B: Identity
5. PR-B1: Identity domain (VO/Entity/Service/Repo interface)
6. PR-B2: Identity schema + repository implementation
7. PR-B3: Identity usecases + gRPC service + error mapping
8. PR-B4: Identity tests (domain/unit/integration)

### Track C: Quiz
9. PR-C1: Quiz domain + category domain
10. PR-C2: Quiz schema + repository
11. PR-C3: Quiz usecases + gRPC service
12. PR-C4: Quiz tests + seed data

### Track D: Session
13. PR-D1: Session domain (category nullable)
14. PR-D2: Session schema + repository + grading adapter
15. PR-D3: Session usecases + gRPC service
16. PR-D4: Session tests + timeout/abandon scenarios

### Track E: Ranking
17. PR-E1: Ranking domain (first-achieved tiebreak)
18. PR-E2: Ranking schema (`rankings`, `rank_entries`) + repository
19. PR-E3: Ranking usecases + gRPC service
20. PR-E4: Ranking tests + index/perf sanity

### Track F: Hardening
21. PR-F1: common error model + interceptor hardening
22. PR-F2: observability baseline (log correlation, metrics)
23. PR-F3: event/outbox extension interfaces and docs
24. PR-F4: release checklist + runbook draft

## Two-Week Execution Plan

### Week 1
- Day 1-2: PR-A1, PR-A2
- Day 3: PR-A3
- Day 4-5: PR-A4, PR-B1

### Week 2
- Day 1-2: PR-B2, PR-B3
- Day 3: PR-B4
- Day 4-5: PR-C1, PR-C2

## Risk Register
- Build chain complexity (proto + multi-module): mitigate with early CI validation.
- Boundary leakage between BCs: enforce ID-only references and PR checklist.
- Reactive stack misuse: keep application orchestration simple and test transaction boundaries.
- Scope blow-up: split PRs aggressively and defer non-critical features to later milestones.

## Review Gates Per PR
- Architecture boundary check passed.
- Domain rule + DB invariant match confirmed.
- Test evidence attached.
- Docs updated in same PR.
- Operational impact noted (migration, config, rollout).
