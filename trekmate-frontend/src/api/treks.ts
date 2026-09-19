import { apiClient } from './client';
import type { Trek, TrekPage, TrekSearchParams, Weather } from '../types/trek';

export async function getTreks(params: TrekSearchParams = {}): Promise<TrekPage> {
  const response = await apiClient.get<TrekPage>('/treks', { params });
  return response.data;
}

export async function getTrek(id: string): Promise<Trek> {
  const response = await apiClient.get<Trek>(`/treks/${id}`);
  return response.data;
}

export async function getWeather(trekId: number): Promise<Weather> {
  const response = await apiClient.get<Weather>(`/weather/${trekId}`);
  return response.data;
}
