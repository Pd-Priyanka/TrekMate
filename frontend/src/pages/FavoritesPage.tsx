import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { getFavorites } from '../api/favorites';
import { EmptyState } from '../components/common/EmptyState';
import { Seo } from '../components/common/Seo';
import { TrekCard } from '../components/treks/TrekCard';
import { TrekSkeleton } from '../components/treks/TrekSkeleton';

export function FavoritesPage() {
  const favorites = useQuery({ queryKey: ['favorites'], queryFn: getFavorites });
  return <section className="animate-enter"><Seo title="Saved treks"/><p className="text-sm font-bold uppercase tracking-wider text-emerald-700 dark:text-emerald-400">Your collection</p><h1 className="mt-2 text-4xl font-black tracking-tight">Saved treks</h1><p className="mt-3 text-slate-600 dark:text-slate-400">Keep the trails you want to come back to.</p>{favorites.isLoading ? <div className="mt-8 grid gap-6 md:grid-cols-2 xl:grid-cols-3">{[1, 2, 3].map((item) => <TrekSkeleton key={item} />)}</div> : favorites.isError ? <p role="alert" className="mt-8 rounded-xl bg-rose-50 p-5 text-rose-700 dark:bg-rose-950/40 dark:text-rose-300">Saved treks could not be loaded. Please try again.</p> : favorites.data?.favorites.length === 0 ? <div className="mt-8"><EmptyState title="You haven't saved any treks yet." description="Explore the catalogue and use the heart button to build your collection." action={<Link to="/treks" className="font-bold text-emerald-700 dark:text-emerald-400">Explore treks</Link>}/></div> : <div className="mt-8 grid gap-6 md:grid-cols-2 xl:grid-cols-3">{favorites.data?.favorites.map((favorite) => <TrekCard key={favorite.trek.id} trek={favorite.trek} />)}</div>}</section>;
}
