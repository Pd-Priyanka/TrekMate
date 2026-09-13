# TrekMate production deployment

This directory deploys the TrekMate frontend, Spring Boot backend, and PostgreSQL database as one production-oriented Docker Compose stack. Nginx is included in the frontend image and provides a single public entry point: it serves the React app and forwards `/api`, `/swagger-ui`, and `/api-docs` to the backend.

## Prerequisites

- Docker Engine 24+ with Docker Compose v2
- The sibling directories must retain this layout:

  ```text
  TrekMate/
  ├── trekmate-backend/
  ├── trekmate-frontend/
  └── trekmate-deployment/
  ```

## Configure secrets

Copy the template and replace every placeholder. Do not commit `.env`.

```bash
cd trekmate-deployment
Copy-Item .env.example .env
```

Set strong, unique `POSTGRES_PASSWORD` and `JWT_SECRET` values. `JWT_SECRET` must be at least 32 characters. Set `OPENWEATHER_API_KEY` if the live weather feature is required.

If port 80 is already used or requires elevated permissions, set `APP_PORT=8088` in `.env`.

## Start and verify

```bash
docker compose up --build -d
docker compose ps
docker compose logs -f backend
```

Open `http://localhost` (or `http://localhost:8088` if you changed `APP_PORT`). Verify the backend through Nginx at:

- `GET /api/v1/health`
- `/swagger-ui.html` when `SPRINGDOC_ENABLED=true`

Flyway automatically applies migrations when the backend starts. PostgreSQL data is stored in the named `postgres-data` volume and survives container recreation. The Compose JDBC connection is internal to the Docker network.

## Operations

```bash
# Follow all service logs
docker compose logs -f

# Stop containers but preserve database data
docker compose down

# Rebuild after source changes
docker compose up --build -d
```

To deliberately remove the database volume, run `docker compose down -v`. This permanently deletes local database data.

## Production notes

- Only Nginx is exposed to the host. PostgreSQL and the backend remain inside the Compose network.
- All services have health checks; the frontend waits for a healthy backend, and the backend waits for a healthy database.
- For an Internet-facing environment, terminate TLS at a reverse proxy/load balancer and route HTTPS traffic to the frontend container. Configure its public hostname and certificate there.
- Back up the PostgreSQL volume/database before upgrades or destructive operations.
