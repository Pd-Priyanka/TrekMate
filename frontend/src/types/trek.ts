export type Difficulty = 'EASY' | 'MODERATE' | 'CHALLENGING' | 'DIFFICULT';

export interface Trek {
  id: number;
  name: string;
  slug: string;
  location: string;
  state: string;
  country: string;
  difficulty: Difficulty;
  distanceKm: number;
  durationDays: number;
  altitudeMeters: number;
  bestSeason: string;
  description: string;
  imageUrl: string | null;
  latitude: number;
  longitude: number;
  createdAt: string;
}

export interface TrekPage {
  content: Trek[];
  totalPages: number;
  totalElements: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}

export interface TrekSearchParams {
  keyword?: string;
  state?: string;
  difficulty?: Difficulty;
  season?: string;
  minDurationDays?: number;
  maxDurationDays?: number;
  sort?: 'name' | 'altitude' | 'distance' | 'newest';
  page?: number;
  size?: number;
}

export interface Weather {
  temperatureCelsius: number;
  humidity: number;
  windSpeed: number;
  rainProbability: number;
  weatherIcon: string;
}
