# TrekMate Frontend

React/Vite/TypeScript foundation with Tailwind, Router, Axios, React Query, React Hook Form readiness, authentication and theme contexts.

```bash
npm install
npm run dev
```

Copy `.env.example` to `.env` when configuring a non-default API base URL.

## Production deployment

The frontend has a multi-stage [Dockerfile](Dockerfile) that builds the Vite bundle and serves it through Nginx, including SPA route fallback. Configure `VITE_API_BASE_URL` at build time with the public backend URL, for example `https://api.trekmate.<domain>/api`. The container is suitable for Docker-based hosting such as Coolify.

## Authentication security note

The current JWT API returns a Bearer token, which the client keeps in `localStorage` to support the existing backend contract. This is protected by a strict same-origin production deployment and automatic token-expiry/401 logout, but it remains more exposed to XSS than an HttpOnly-cookie session. A future backend-supported HttpOnly-cookie refresh-token design would be the preferred upgrade; do not attempt to move tokens into cookies without corresponding backend CSRF and refresh-token support.
