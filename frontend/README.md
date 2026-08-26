# TrekMate Frontend

React/Vite/TypeScript foundation with Tailwind, Router, Axios, React Query, React Hook Form readiness, authentication and theme contexts.

```bash
npm install
npm run dev
```

Copy `.env.example` to `.env` when configuring a non-default API base URL.

## Production deployment

The frontend has a multi-stage [Dockerfile](Dockerfile) that builds the Vite bundle and serves it through Nginx. The Nginx configuration proxies backend API requests through the same origin. Run the complete deployment stack from the sibling [deployment directory](../trekmate-deployment/README.md).

## Authentication security note

The current JWT API returns a Bearer token, which the client keeps in `localStorage` to support the existing backend contract. This is protected by a strict same-origin production deployment and automatic token-expiry/401 logout, but it remains more exposed to XSS than an HttpOnly-cookie session. A future backend-supported HttpOnly-cookie refresh-token design would be the preferred upgrade; do not attempt to move tokens into cookies without corresponding backend CSRF and refresh-token support.
