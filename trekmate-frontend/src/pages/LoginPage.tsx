import { AxiosError } from 'axios';
import { useForm } from 'react-hook-form';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { login } from '../api/auth';
import { AuthCard } from '../components/auth/AuthCard';
import { useAuth } from '../contexts/AuthContext';
import type { LoginRequest } from '../types/auth';

export function LoginPage() {
  const { completeLogin } = useAuth(); const navigate = useNavigate(); const location = useLocation();
  const { register, handleSubmit, formState: { errors, isSubmitting }, setError } = useForm<LoginRequest>({ defaultValues: { email: '', password: '' } });
  const submit = async (values: LoginRequest) => { try { completeLogin(await login(values)); navigate((location.state as { from?: string } | null)?.from ?? '/'); } catch (error) { const message = error instanceof AxiosError ? error.response?.data?.message : undefined; setError('root', { message: message || 'Email or password is incorrect.' }); } };
  return <AuthCard title="Welcome back" subtitle="Sign in to save treks, view conditions, and keep planning."><form onSubmit={handleSubmit(submit)} noValidate className="space-y-5"><Field label="Email address" error={errors.email?.message}><input type="email" autoComplete="email" {...register('email', { required: 'Enter your email address.', pattern: { value: /^\S+@\S+\.\S+$/, message: 'Enter a valid email address.' } })} /></Field><Field label="Password" error={errors.password?.message}><input type="password" autoComplete="current-password" {...register('password', { required: 'Enter your password.' })} /></Field>{errors.root && <p className="rounded-lg bg-rose-50 p-3 text-sm text-rose-700 dark:bg-rose-950/50 dark:text-rose-300">{errors.root.message}</p>}<button disabled={isSubmitting} className="w-full rounded-xl bg-emerald-600 px-4 py-3 font-bold text-white hover:bg-emerald-500 disabled:opacity-60">{isSubmitting ? 'Signing in…' : 'Sign in'}</button></form><p className="mt-6 text-center text-sm text-slate-600 dark:text-slate-400">New to TrekMate? <Link to="/register" className="font-bold text-emerald-700 dark:text-emerald-400">Create an account</Link></p></AuthCard>;
}
function Field({ label, error, children }: { label: string; error?: string; children: React.ReactNode }) { return <label className="block text-sm font-semibold">{label}<span className="mt-2 block [&_input]:w-full [&_input]:rounded-xl [&_input]:border [&_input]:border-slate-300 [&_input]:bg-transparent [&_input]:px-3 [&_input]:py-3 [&_input]:font-normal [&_input]:outline-none [&_input]:focus:border-emerald-500 dark:[&_input]:border-slate-700">{children}</span>{error && <span className="mt-1 block text-xs font-normal text-rose-600 dark:text-rose-400">{error}</span>}</label>; }
