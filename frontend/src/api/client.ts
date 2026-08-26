import axios from 'axios';
export const apiClient = axios.create({ baseURL: import.meta.env.VITE_API_BASE_URL ?? '/api', headers: { 'Content-Type': 'application/json' } });
apiClient.interceptors.request.use((config) => { const token = localStorage.getItem('trekmate.accessToken'); if (token) config.headers.Authorization = `Bearer ${token}`; return config; });
apiClient.interceptors.response.use((response) => response, (error) => { if (error.response?.status === 401) window.dispatchEvent(new Event('trekmate:logout')); return Promise.reject(error); });
