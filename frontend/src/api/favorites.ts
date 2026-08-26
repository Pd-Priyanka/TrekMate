import { apiClient } from './client';
import type { Trek } from '../types/trek';

interface FavoriteItem { id: number; trek: Trek; createdAt: string }
export interface FavoritesResponse { favoriteCount: number; favorites: FavoriteItem[] }

export async function getFavorites(): Promise<FavoritesResponse> {
  const response = await apiClient.get<FavoritesResponse>('/favorites');
  return response.data;
}

export async function addFavorite(trekId: number): Promise<void> {
  await apiClient.post(`/favorites/${trekId}`);
}

export async function removeFavorite(trekId: number): Promise<void> {
  await apiClient.delete(`/favorites/${trekId}`);
}
