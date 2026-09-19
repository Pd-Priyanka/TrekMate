import { apiClient } from './client';

export interface Review { id: number; rating: number; comment: string; createdAt: string; userId: number; userName: string }
export interface ReviewSummary { averageRating: number; reviewCount: number; reviews: Review[] }
export interface ReviewRequest { rating: number; comment: string }

export async function getReviews(trekId: number): Promise<ReviewSummary> { return (await apiClient.get(`/treks/${trekId}/reviews`)).data; }
export async function createReview(trekId: number, request: ReviewRequest): Promise<Review> { return (await apiClient.post(`/treks/${trekId}/reviews`, request)).data; }
export async function updateReview(id: number, request: ReviewRequest): Promise<Review> { return (await apiClient.put(`/reviews/${id}`, request)).data; }
export async function deleteReview(id: number): Promise<void> { await apiClient.delete(`/reviews/${id}`); }
