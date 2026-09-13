# TrekMate Production Deployment

## Architecture

```text
Internet
└── VPS / Coolify
    ├── trekmate.<domain>      → React / Vite frontend container
    ├── api.trekmate.<domain>  → Spring Boot backend container
    └── PostgreSQL             → private Coolify resource
```

Coolify manages HTTPS certificates and routes both custom domains to their respective containers. PostgreSQL remains on the private Docker network and is not publicly exposed.

## Prerequisites

- A Git repository containing the TrekMate monorepo
- An Ubuntu VPS with Coolify installed
- A PostgreSQL resource in Coolify
- Two DNS records for `trekmate.<domain>` and `api.trekmate.<domain>` pointing to the VPS
- A generated JWT secret of at least 32 random characters
- An OpenWeather API key if live weather is required

## PostgreSQL

Create a PostgreSQL resource in Coolify. Use its private-network hostname in the backend JDBC URL; do not expose the database port publicly. On first backend startup, Flyway applies versions `V1` through `V8` and inserts the ten public treks. It does not create users or administrator credentials.

## Backend Application

Create a Dockerfile-based application with root directory `trekmate-backend`. The existing [Dockerfile](../trekmate-backend/Dockerfile) builds and starts the Spring Boot JAR.

| Setting | Value |
| --- | --- |
| Root directory | `trekmate-backend` |
| Dockerfile | `Dockerfile` |
| Internal port | `8080` |
| Health endpoint | `/api/v1/health` |
| Spring profile | `prod` |
| Public domain | `api.trekmate.<domain>` |

## Backend Environment Variables

| Variable | Required | Purpose | Example format |
| --- | --- | --- | --- |
| `SPRING_PROFILES_ACTIVE` | Yes | Enables production settings | `prod` |
| `DB_URL` | Yes | PostgreSQL JDBC URL on the private network | `jdbc:postgresql://postgres:5432/trekmate` |
| `DB_USERNAME` | Yes | PostgreSQL user | database user name |
| `DB_PASSWORD` | Yes | PostgreSQL password | secret value |
| `JWT_SECRET` | Yes | JWT signing key; at least 32 random characters | generated secret |
| `FRONTEND_URL` | Yes | Exact frontend HTTPS origin | `https://trekmate.<domain>` |
| `OPENWEATHER_API_KEY` | Recommended | Enables live weather | OpenWeather key |
| `JWT_EXPIRATION_MS` | Optional | JWT lifetime | `86400000` |
| `DB_POOL_MAX_SIZE` | Optional | Hikari upper pool size | `5` |
| `DB_POOL_MIN_IDLE` | Optional | Hikari minimum idle connections | `1` |
| `DB_CONNECTION_TIMEOUT_MS` | Optional | Database connection timeout | `30000` |
| `SPRINGDOC_ENABLED` | Optional | Enables Swagger | `true` or `false` |
| `LOG_LEVEL_ROOT` | Optional | Root log level | `WARN` |
| `LOG_LEVEL_APP` | Optional | TrekMate log level | `INFO` |

## Frontend Application

Create a Dockerfile-based application with root directory `trekmate-frontend`. Its Nginx container provides static hosting and SPA fallback.

| Setting | Value |
| --- | --- |
| Root directory | `trekmate-frontend` |
| Dockerfile | `Dockerfile` |
| Internal port | `80` |
| Public domain | `trekmate.<domain>` |

Set this build-time frontend variable:

| Variable | Value |
| --- | --- |
| `VITE_API_BASE_URL` | `https://api.trekmate.<domain>/api` |

`VITE_*` values are public browser configuration. Never place database credentials, JWT secrets, or weather API keys in them.

## CORS and First Deployment

1. Create the PostgreSQL resource.
2. Configure and deploy the backend with `FRONTEND_URL=https://trekmate.<domain>`.
3. Verify `GET https://api.trekmate.<domain>/api/v1/health` returns `200`.
4. Configure and deploy the frontend with `VITE_API_BASE_URL=https://api.trekmate.<domain>/api`.
5. Verify browser requests include the Authorization header and pass CORS preflight.

## Smoke Tests

- Health endpoint returns `200`.
- Authentication, trek browsing, favorites, and reviews work.
- Weather either returns data or a controlled `503` when unavailable.
- Browser refresh works on nested routes.
- Images load over HTTPS and retain their UI fallback.

## Rollback

Deploy the last known-good Git revision through Coolify. Do not remove Flyway history or database tables as part of a rollback.
