import { Link } from 'react-router-dom';
import type { Trek } from '../../types/trek';
import { DifficultyBadge } from './DifficultyBadge';
import { FavoriteButton } from './FavoriteButton';
import { TrekImage } from './TrekImage';

export function TrekCard({ trek }: { trek: Trek }) {
  return <article className="group overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm transition hover:-translate-y-1 hover:shadow-lg dark:border-slate-800 dark:bg-slate-900">
    <div className="relative flex h-48 items-end overflow-hidden bg-gradient-to-br from-emerald-900 via-teal-700 to-sky-700 p-5">
      <TrekImage trek={trek} className="absolute inset-0 h-full w-full object-cover transition duration-500 group-hover:scale-105" />
      <div className="absolute inset-0 bg-gradient-to-t from-slate-950/75 to-transparent" />
      <div className="relative flex w-full items-end justify-between gap-3"><div><p className="text-sm font-medium text-white/80">{trek.location}, {trek.state}</p><h2 className="mt-1 text-xl font-bold text-white">{trek.name}</h2></div><FavoriteButton trekId={trek.id} /></div>
    </div>
    <div className="p-5"><div className="flex items-center justify-between gap-2"><DifficultyBadge difficulty={trek.difficulty} /><span className="text-sm text-slate-500 dark:text-slate-400">{trek.durationDays} days</span></div><p className="mt-4 line-clamp-2 text-sm leading-6 text-slate-600 dark:text-slate-300">{trek.description}</p><dl className="mt-5 grid grid-cols-2 gap-3 border-t border-slate-100 pt-4 text-sm dark:border-slate-800"><div><dt className="text-slate-500">Distance</dt><dd className="mt-1 font-semibold">{trek.distanceKm} km</dd></div><div><dt className="text-slate-500">Altitude</dt><dd className="mt-1 font-semibold">{trek.altitudeMeters.toLocaleString()} m</dd></div></dl><Link to={`/treks/${trek.id}`} className="mt-5 inline-flex font-semibold text-emerald-700 hover:text-emerald-600 dark:text-emerald-400">Explore trek <span className="ml-1" aria-hidden="true">→</span></Link></div>
  </article>;
}
