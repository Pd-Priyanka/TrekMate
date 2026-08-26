import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useLocation, useNavigate } from 'react-router-dom';
import { addFavorite, getFavorites, removeFavorite } from '../../api/favorites';
import { useAuth } from '../../contexts/AuthContext';

export function FavoriteButton({ trekId }: { trekId: number }) {
  const { token } = useAuth(); const queryClient = useQueryClient(); const navigate = useNavigate(); const location = useLocation();
  const favorites = useQuery({ queryKey: ['favorites'], queryFn: getFavorites, enabled: Boolean(token), retry: false });
  const isFavorite = favorites.data?.favorites.some((favorite) => favorite.trek.id === trekId) ?? false;
  const mutation = useMutation({ mutationFn: () => isFavorite ? removeFavorite(trekId) : addFavorite(trekId), onSuccess: () => queryClient.invalidateQueries({ queryKey: ['favorites'] }) });
  if (!token) return <button type="button" onClick={(event) => { event.preventDefault(); navigate('/login', { state: { from: `${location.pathname}${location.search}` } }); }} aria-label="Sign in to add to favorites" className="grid h-9 w-9 place-items-center rounded-full bg-white/90 text-slate-700 backdrop-blur hover:bg-white">♡</button>;
  return <div className="relative"><button type="button" onClick={(event) => { event.preventDefault(); mutation.mutate(); }} disabled={mutation.isPending} aria-label={isFavorite ? 'Remove from favorites' : 'Add to favorites'} className={`grid h-9 w-9 place-items-center rounded-full backdrop-blur transition disabled:cursor-wait ${isFavorite ? 'bg-rose-500 text-white' : 'bg-white/90 text-slate-700 hover:bg-white'}`}>{isFavorite ? '♥' : '♡'}</button>{mutation.isError && <span role="alert" className="sr-only">Unable to update favorites. Please try again.</span>}</div>;
}
