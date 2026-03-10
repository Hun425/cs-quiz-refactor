# Stack Baseline (Canonical)

## Purpose
이 문서는 프로젝트의 기술 버전 기준선과 선택 이유를 기록한다.
버전 업그레이드/다운그레이드 판단은 이 문서를 기준으로 수행한다.

## Current Baseline (2026-03-10)
- Kotlin: 2.3.10
- Spring Boot: 4.0.3
- Armeria: 1.36.0
- gRPC Java: 1.73.0
- gRPC Kotlin: 1.4.3
- Protocol Buffers: 4.31.1
- jOOQ: 3.20.5
- R2DBC SPI: 1.0.0.RELEASE
- PostgreSQL JDBC: 42.7.7
- JDK target: 21

## Why This Baseline
- Spring Boot 4.x 계열은 Kotlin 2.2+ 메타데이터와의 호환을 전제로 동작한다.
- Armeria는 Boot 4 대응 starter(`armeria-spring-boot4-starter`)를 사용한다.
- Netty 계열은 `netty.version=4.2.6.Final`로 정렬해 클래스 충돌을 방지한다.
- JDK 21을 공통 기준으로 잡아 언어/라이브러리 기능 사용 범위를 명확히 한다.

## Upgrade Policy
- 원칙: 최신 안정 버전 우선, 단 "전체 build 통과"가 전제.
- 방식: 한 PR에서 버전 변경 + 호환성 수정 + 검증 로그를 함께 처리.
- 금지: 버전만 올리고 깨진 상태로 병합 금지.

## Compatibility Rules
- Spring Boot major 업그레이드 시 Kotlin 버전 동시 검토 필수.
- Armeria starter는 Boot major와 동일 라인으로 맞춘다.
- Netty 이슈 발생 시 dependency insight로 실제 선택 버전 확인 후 정렬한다.

## PR Checklist For Version Changes
- [ ] `gradle.properties` 버전 동기화
- [ ] 관련 build script 플러그인/starter 동기화
- [ ] `gradle clean build` 통과 확인
- [ ] 이 문서(`docs/STACK_BASELINE.md`) 업데이트
- [ ] PR "Design Notes"에 호환성 영향 기록
