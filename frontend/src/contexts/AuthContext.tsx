import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from 'react';
import type { AuthResponse, User } from '../types/auth';

const TOKEN_KEY = 'trekmate.accessToken'; const USER_KEY = 'trekmate.user'; const LOGOUT_EVENT = 'trekmate:logout';
type AuthContextValue = { token: string | null; user: User | null; isAuthenticated: boolean; completeLogin: (response: AuthResponse) => void; logout: () => void };
const AuthContext = createContext<AuthContextValue | null>(null);

function readStoredUser(): User | null { try { const raw = localStorage.getItem(USER_KEY); return raw ? JSON.parse(raw) as User : null; } catch { return null; } }
function getExpiry(token: string): number | null { try { const payloadSegment = token.split('.')[1]; if (!payloadSegment) return null; const normalized = payloadSegment.replace(/-/g, '+').replace(/_/g, '/'); const payload = JSON.parse(atob(normalized.padEnd(normalized.length + (4 - normalized.length % 4) % 4, '='))) as { exp?: number }; return payload.exp ? payload.exp * 1000 : null; } catch { return null; } }
function isExpired(token: string | null) { const expiry = token && getExpiry(token); return !expiry || expiry <= Date.now(); }

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, updateToken] = useState<string | null>(() => { const stored = localStorage.getItem(TOKEN_KEY); return isExpired(stored) ? null : stored; });
  const [user, updateUser] = useState<User | null>(() => isExpired(localStorage.getItem(TOKEN_KEY)) ? null : readStoredUser());
  const logout = () => { localStorage.removeItem(TOKEN_KEY); localStorage.removeItem(USER_KEY); updateToken(null); updateUser(null); };
  const completeLogin = (response: AuthResponse) => { localStorage.setItem(TOKEN_KEY, response.accessToken); localStorage.setItem(USER_KEY, JSON.stringify(response.user)); updateToken(response.accessToken); updateUser(response.user); };
  useEffect(() => { if (!token) return; const expiry = getExpiry(token); if (!expiry) return; const timer = window.setTimeout(logout, Math.max(0, expiry - Date.now())); return () => window.clearTimeout(timer); }, [token]);
  useEffect(() => { window.addEventListener(LOGOUT_EVENT, logout); return () => window.removeEventListener(LOGOUT_EVENT, logout); }, []);
  const value = useMemo(() => ({ token, user, isAuthenticated: Boolean(token), completeLogin, logout }), [token, user]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
export const useAuth = () => { const context = useContext(AuthContext); if (!context) throw new Error('useAuth must be used within AuthProvider'); return context; };
