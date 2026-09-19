import type { ReactNode } from 'react';

export function AuthCard({ title, subtitle, children }: { title: string; subtitle: string; children: ReactNode }) {
  return <section className="mx-auto my-8 w-full max-w-md rounded-3xl border border-slate-200 bg-white p-6 shadow-xl shadow-slate-200/50 sm:p-8 dark:border-slate-800 dark:bg-slate-900 dark:shadow-none"><p className="text-sm font-bold uppercase tracking-[.2em] text-emerald-600 dark:text-emerald-400">TrekMate</p><h1 className="mt-4 text-3xl font-black tracking-tight">{title}</h1><p className="mt-3 text-sm leading-6 text-slate-600 dark:text-slate-400">{subtitle}</p><div className="mt-7">{children}</div></section>;
}
