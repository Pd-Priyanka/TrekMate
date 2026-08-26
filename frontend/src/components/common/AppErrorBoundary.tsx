import { Component, type ErrorInfo, type ReactNode } from 'react';

interface Props { children: ReactNode }
interface State { hasError: boolean }

export class AppErrorBoundary extends Component<Props, State> {
  public state: State = { hasError: false };

  public static getDerivedStateFromError(): State {
    return { hasError: true };
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error('Uncaught application error', error, errorInfo);
  }

  public render() {
    if (this.state.hasError) {
      return <main className="mx-auto grid min-h-screen max-w-xl place-items-center px-6 text-center"><section><p className="text-sm font-bold uppercase tracking-widest text-emerald-600">TrekMate</p><h1 className="mt-4 text-3xl font-black">We hit an unexpected trail block.</h1><p className="mt-3 text-slate-600 dark:text-slate-400">Refresh to try again. If the issue continues, return home and start a new search.</p><div className="mt-7 flex justify-center gap-3"><button onClick={() => window.location.reload()} className="rounded-xl bg-emerald-600 px-4 py-2.5 font-bold text-white hover:bg-emerald-500">Refresh page</button><a href="/" className="rounded-xl border border-slate-300 px-4 py-2.5 font-bold dark:border-slate-700">Return home</a></div></section></main>;
    }
    return this.props.children;
  }
}
