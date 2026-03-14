# Local DB

## Purpose
로컬 PostgreSQL 실행과 애플리케이션 연결 기준을 문서화한다.

## Local Stack
- PostgreSQL 17 (`compose.yaml`)
- JDBC + R2DBC 동시 사용
- Flyway migration location: `classpath:db/migration`

## Run
1. `docker compose up -d postgres`
2. 애플리케이션 실행 시 `local` 프로필 활성화
3. 예시: `gradle -p . :bootstrap:bootRun --args='--spring.profiles.active=local'`

## Default Local Credentials
- database: `cs_quiz`
- username: `csquiz`
- password: `csquiz`
- port: `5432`

## Notes
- 기본 프로필(`application.yml`)에는 DB 연결 정보를 두지 않는다.
- 실제 DB 연결/Flyway는 `application-local.yml`에서만 활성화한다.
