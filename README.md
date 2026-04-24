# RescuEat Backend

Spring Boot backend for RescuEat (food deals API) with PostgreSQL + JWT auth.

## Requirements

- **Java 17**+
- PostgreSQL (for local run and production)
- Gradle wrapper included (`./gradlew`)

## Configuration model

The app now supports environment-driven configuration.

### Required environment variables (production)

- `DB_URL` (example: `jdbc:postgresql://db-host:5432/rescureat_db`)
- `DB_USER`
- `DB_PASS`
- `JWT_SECRET` (long random secret)
- `CORS_ALLOWED_ORIGINS` (comma-separated, e.g. `https://app.example.com,https://admin.example.com`; use `*` only if you intentionally want wildcard origin-pattern behavior)

### Local development defaults

`application.properties` includes local-safe defaults so you can run quickly on your machine:
- DB defaults to local PostgreSQL URL and dev credentials
- CORS defaults to `http://localhost:3000,https://rescureat-frontend.vercel.app`
- JWT secret has a dev-only fallback

For real deployments, use `SPRING_PROFILES_ACTIVE=prod` and set all required env vars.

## Profiles

- **default/dev-ish**: `spring.jpa.hibernate.ddl-auto=update` for local convenience.
- **prod** (`application-prod.properties`):
  - `spring.jpa.hibernate.ddl-auto=validate`
  - fails fast if schema does not match entities
  - safer for production; pair with explicit DB migrations (Flyway/Liquibase)

## Run locally

1) Ensure PostgreSQL is available.
2) (Optional) export env vars if you want to override defaults:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/rescureat_db
export DB_USER=rescureat_dev
export DB_PASS=rescureat_dev
export JWT_SECRET='replace-with-long-random-dev-secret'
export CORS_ALLOWED_ORIGINS='http://localhost:3000,https://rescureat-frontend.vercel.app'
```

3) Start app:

```bash
./gradlew bootRun
```

API base URL: `http://localhost:8080`

## Deploy (production)

Set profile and env vars:

```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_URL='jdbc:postgresql://<host>:5432/<db>'
export DB_USER='<user>'
export DB_PASS='<pass>'
export JWT_SECRET='<long-random-secret>'
export CORS_ALLOWED_ORIGINS='https://app.example.com,https://admin.example.com'
```

Then run:

```bash
java -jar build/libs/rescureat-backend-0.0.1-SNAPSHOT.jar
```

## Health endpoint

Actuator is enabled with:
- `GET /actuator/health`

This is suitable for deployment liveness/readiness checks.

## Build and test

```bash
./gradlew build
./gradlew test
```
