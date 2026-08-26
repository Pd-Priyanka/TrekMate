import { Link, useRouteError } from 'react-router-dom';
export function ErrorPage() { const error = useRouteError() as Error; return <section><h1 className="text-2xl font-bold">Something went wrong</h1><p className="mt-2 text-slate-600 dark:text-slate-400">{error?.message ?? 'Please try again later.'}</p><Link to="/" className="mt-4 inline-block text-emerald-700">Return home</Link></section>; }
