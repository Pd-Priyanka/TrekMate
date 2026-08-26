import { lazy, Suspense, type ReactNode } from 'react';
import { createBrowserRouter } from 'react-router-dom';
import { PageLoader } from '../components/common/PageLoader';
import { AppLayout } from '../components/layout/AppLayout';
import { ProtectedRoute } from '../components/auth/ProtectedRoute';
import { ErrorPage } from '../pages/ErrorPage';

const HomePage = lazy(async () => ({ default: (await import('../pages/HomePage')).HomePage }));
const FavoritesPage = lazy(async () => ({ default: (await import('../pages/FavoritesPage')).FavoritesPage }));
const LoginPage = lazy(async () => ({ default: (await import('../pages/LoginPage')).LoginPage }));
const NotFoundPage = lazy(async () => ({ default: (await import('../pages/NotFoundPage')).NotFoundPage }));
const ProfilePage = lazy(async () => ({ default: (await import('../pages/ProfilePage')).ProfilePage }));
const RegisterPage = lazy(async () => ({ default: (await import('../pages/RegisterPage')).RegisterPage }));
const TrekDetailsPage = lazy(async () => ({ default: (await import('../pages/TrekDetailsPage')).TrekDetailsPage }));
const TrekListPage = lazy(async () => ({ default: (await import('../pages/TrekListPage')).TrekListPage }));

function page(component: ReactNode) { return <Suspense fallback={<PageLoader />}>{component}</Suspense>; }

export const router = createBrowserRouter([{ path: '/', element: <AppLayout />, errorElement: <ErrorPage />, children: [{ index: true, element: page(<HomePage />) }, { path: 'treks', element: page(<TrekListPage />) }, { path: 'treks/:trekId', element: page(<TrekDetailsPage />) }, { path: 'login', element: page(<LoginPage />) }, { path: 'register', element: page(<RegisterPage />) }, { element: <ProtectedRoute />, children: [{ path: 'profile', element: page(<ProfilePage />) }, { path: 'favorites', element: page(<FavoritesPage />) }] }, { path: '*', element: page(<NotFoundPage />) }] }]);
