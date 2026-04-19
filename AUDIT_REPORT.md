# RescuEat Backend Audit (MVP + Deployment Readiness)

Date: 2026-04-18

This report audits implemented API surface, authn/authz, persistence, and deployment readiness for the current Spring Boot backend.

## 1) Endpoint Inventory and Access Classification

### Public endpoints
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/deals`
- `GET /api/deals/{id}`
- `GET /api/deals/nearby`

### Requires auth (any authenticated role)
- `POST /api/reservations`
- `GET /api/reservations`

### Requires role `CAFE_OWNER`
- `POST /api/deals`
- `DELETE /api/deals/{id}`

## 2) Authentication/Authorization Verification

- JWT generation: in `AuthService.register` and `AuthService.login` via `jwtService.generateToken(user)`.
- JWT secret source: `jwt.secret` property bound through `JwtProperties`.
- JWT validation: `JwtAuthenticationFilter` reads `Authorization: Bearer <token>`, extracts email subject, loads user details, validates signature/expiration, and sets Spring Security context.
- Role model: enum has `STUDENT` and `CAFE_OWNER`; authority is mapped to `ROLE_<role>`.
- Enforcement is URL-pattern based in `SecurityConfig`.

### Notable security gap
- `GET /api/reservations` returns all reservations for all users (global list), not scoped to currently authenticated user.

## 3) Persistence Verification

### Entities / tables
- `User` -> table `app_users`
- `FoodListing` -> table `food_listings`
- `Reservation` -> table `reservations`

### Repository layer
- `UserRepository`, `FoodListingRepository`, `ReservationRepository` (with `findAllWithUser`, `deleteByDealId`).

### Seed strategy
- `DataSeederConfig` inserts 3 deals only when `food_listing` count is 0.

### Reservation ownership model
- Reservation now links to `User` entity (`@ManyToOne user` with `user_id`), and exposes `userId` in JSON.
- Reservation no longer stores plain `userName` in DB field; username in response is derived from linked user relation.

### Data integrity risks
- `Reservation.dealId` is scalar `Long`, not relational FK to `FoodListing`; referential integrity to deals is app-enforced.
- Deal delete manually cascades by deleting reservations first in service; no DB-level cascade guarantees.

## 4) Deployment Readiness Findings

- `application.properties` has hardcoded localhost DB URL/username/password defaults.
- JWT has environment override (`JWT_SECRET`) but also a hardcoded fallback default secret.
- CORS currently allows only `http://localhost:3000`.
- No Spring profile split for production config observed (`application-dev.properties` exists but minimal).
- No actuator dependency and no health endpoint currently configured.

## 5) MVP Priority Fix Plan (ordered)

1. Scope reservations to current user (and optional owner/admin views) instead of global list.
2. Enforce role/ownership boundaries for reservation visibility and future updates/cancels.
3. Remove hardcoded DB creds and JWT fallback secret from defaults; require env vars in production profile.
4. Add production profile (`application-prod.properties`) and tighten `ddl-auto` strategy (`validate` or migrations).
5. Replace hardcoded CORS origin with env-driven allowed origins list.
6. Add DB-level FK from reservations to listings (model relation) or enforce integrity with migrations.
7. Add explicit reservation uniqueness/stock constraints (prevent overbooking/duplicates per user-deal if needed).
8. Add health checks (`spring-boot-starter-actuator`, expose `/actuator/health`).
9. Add structured logging config and log levels per profile.
10. Add integration tests (auth flow, role checks, reservations scoping, destructive operations).

## 6) Minimal Changes Required to Deploy Backend

- Externalize env vars: `DB_URL`, `DB_USER`, `DB_PASS`, `JWT_SECRET`, `CORS_ALLOWED_ORIGINS`.
- Add `application-prod.properties` and set `spring.profiles.active=prod` in deployment.
- Ensure PostgreSQL instance exists and schema is initialized/migrated.
- Add health endpoint for platform probes.
- Update frontend to send bearer tokens for protected endpoints.

