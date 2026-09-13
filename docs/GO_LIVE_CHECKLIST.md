# TrekMate Go-Live Checklist

## Pre-deployment

- [ ] CI is green.
- [ ] Backend tests and package build pass.
- [ ] Frontend TypeScript/Vite build passes.
- [ ] No secrets are committed.
- [ ] `.env.example` files contain placeholders only.
- [ ] Production configuration is reviewed.

## Database

- [ ] Coolify PostgreSQL resource is created.
- [ ] Backend PostgreSQL variables are referenced correctly.
- [ ] Flyway is enabled.
- [ ] Migrations `V1`–`V8` are verified in backend logs.
- [ ] Ten seeded treks are visible.

## Backend

- [ ] Coolify backend application uses root directory `trekmate-backend`.
- [ ] `SPRING_PROFILES_ACTIVE=prod` is configured.
- [ ] `JWT_SECRET` is configured with a strong secret.
- [ ] `OPENWEATHER_API_KEY` is configured if weather is enabled.
- [ ] `/api/v1/health` works without authentication.
- [ ] `api.trekmate.<domain>` is configured with HTTPS.

## Frontend

- [ ] Coolify frontend application uses root directory `frontend`.
- [ ] `VITE_API_BASE_URL` points to the backend `/api` URL.
- [ ] Frontend production build succeeds.
- [ ] Frontend is deployed.

## CORS

- [ ] The exact frontend HTTPS origin is set in backend `FRONTEND_URL`.
- [ ] `localhost` is not used as the production CORS origin.
- [ ] Browser preflight and Authorization header requests work.

## Functional testing

- [ ] Registration and login work.
- [ ] Trek browsing, filtering, and search work.
- [ ] Trek details and images work.
- [ ] Favorites work.
- [ ] Reviews work.
- [ ] Weather works or degrades with a controlled `503`.
- [ ] Logout works.

## Responsive testing

- [ ] Mobile layout works.
- [ ] Tablet layout works.
- [ ] Desktop layout works.

## Security

- [ ] Admin trek mutation APIs remain protected.
- [ ] Review ownership is enforced.
- [ ] No backend secrets are in frontend `VITE_*` variables.
- [ ] Swagger exposure is reviewed (`SPRINGDOC_ENABLED`).
- [ ] Only the custom health endpoint is public; no broad actuator exposure exists.

## Post-deployment

- [ ] Backend and frontend logs are checked.
- [ ] Flyway schema history is checked.
- [ ] Browser console and network errors are checked.
- [ ] README live URL is updated when available.
- [ ] Portfolio live-demo URL is updated when applicable.
