import { useState } from 'react';
import type { Trek } from '../../types/trek';

export function TrekImage({ trek, className, loading = 'lazy' }: { trek: Trek; className: string; loading?: 'lazy' | 'eager' }) {
  const [failed, setFailed] = useState(false);
  if (!trek.imageUrl || failed) return null;
  return <img src={trek.imageUrl} alt={`${trek.name} trek in ${trek.state}`} loading={loading} decoding="async" onError={() => setFailed(true)} className={className} />;
}
