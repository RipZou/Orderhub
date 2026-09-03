# OrderHub

Mini order system built with Spring Boot for backend learning / New Grad interview prep.

Features: user auth (JWT), role-based access (USER / ADMIN), product catalog, order lifecycle with ownership checks, PostgreSQL persistence.

## Stack

- Java 21
- Spring Boot 3.4
- Spring Security (stateless JWT)
- Spring Data JPA + PostgreSQL
- Maven

## Architecture

```text
Controller  →  Service  →  JpaRepository  →  PostgreSQL
                 ↑
         JwtAuthFilter + Role (USER / ADMIN)
```

- **Authentication**: JWT Bearer token (`sub` = userId, `role` claim)
- **Authorization**: `SecurityConfig.hasRole` for admin actions + service-level order ownership checks
- **Domain**: order state machine (`CREATED → PAID → SHIPPED → COMPLETED`, cancel rules)

## Prerequisites

- JDK 21
- Maven 3.9+
- Docker (for PostgreSQL) or a local Postgres instance

## Start PostgreSQL

Default config in `application.yml`:

| Setting  | Value    |
|----------|----------|
| Host     | localhost |
| Port     | **5433** |
| Database | orderhub |
| User     | orderhub |
| Password | orderhub |

```bash
docker run -d --name orderhub-pg \
  -e POSTGRES_DB=orderhub \
  -e POSTGRES_USER=orderhub \
  -e POSTGRES_PASSWORD=orderhub \
  -p 5433:5432 \
  postgres:16
```

## Run the app

```bash
mvn spring-boot:run
```

Or run `OrderHubApplication` from IntelliJ IDEA.

App listens on `http://localhost:8080`.

## Quick API flow

Base URL: `http://localhost:8080`

Protected endpoints need:

```http
Authorization: Bearer <token>
```

In Postman: Auth type = Bearer Token; paste the token only (do not include the `Bearer ` prefix in the token field).

### 1. Register

`POST /auth/register`

```json
{
  "id": "u1",
  "email": "user@example.com",
  "name": "Alice",
  "password": "password123"
}
```

New users get role `USER`. Password hash is never returned.

### 2. Login

`POST /auth/login`

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

Response:

```json
{
  "token": "<jwt>"
}
```

After changing a user's role in the DB, **login again** — old tokens do not pick up the new role.

### 3. Promote a user to ADMIN (local testing)

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'user@example.com';
```

Then login again and use the new token.

### 4. Create a product (ADMIN only)

`POST /products`

```json
{
  "productId": "p1",
  "productName": "Keyboard",
  "productPrice": 99.0,
  "productStock": 50
}
```

- USER → **403**
- ADMIN → **200**

### 5. Place an order (authenticated)

`POST /orders`

Buyer id comes from the JWT — do **not** send `buyerId` in the body.

```json
{
  "orderId": "o1",
  "productId": ["p1"],
  "quantities": [1]
}
```

Use a unique `orderId` each time (duplicates → **409**).

### 6. Order actions

| Method | Path | Who |
|--------|------|-----|
| `POST` | `/orders/{id}/pay` | order owner |
| `POST` | `/orders/{id}/cancel` | order owner |
| `POST` | `/orders/{id}/ship` | **ADMIN** |
| `POST` | `/orders/{id}/complete` | **ADMIN** |
| `GET` | `/orders/{id}` | order owner |
| `GET` | `/orders` | current user's orders |
| `GET` | `/products/{id}` | authenticated |

## Roles

| Role | Can do |
|------|--------|
| `USER` | register/login, place/pay/cancel own orders, list own orders, get products |
| `ADMIN` | everything USER can + create products, ship, complete |

Order ownership is enforced in the service layer: even an authenticated user cannot pay/cancel someone else's order (**403**).

## Common status codes

| Code | Meaning |
|------|---------|
| 401 | Missing / invalid token |
| 403 | Authenticated but not allowed (wrong role or not order owner) |
| 409 | Conflict (e.g. duplicate `orderId`) |

## Project layout

```text
src/main/java/com/orderhub/
  controller/   # HTTP + DTO mapping
  service/      # business logic
  domain/       # entities + state machine
  repository/   # Spring Data JPA
  dto/          # request/response models
  config/       # Security + JWT filter
  common/       # global exception handling
```

## Notes

- JWT secret and expiration live under `orderhub.jwt` in `application.yml` (replace before any real deployment).
- Schema is managed with `spring.jpa.hibernate.ddl-auto: update` for local development.
