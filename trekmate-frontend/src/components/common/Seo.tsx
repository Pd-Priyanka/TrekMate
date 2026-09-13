import { useEffect } from 'react';

const DEFAULT_DESCRIPTION = 'Discover and plan unforgettable treks across India with TrekMate.';

export function Seo({ title, description = DEFAULT_DESCRIPTION }: { title: string; description?: string }) {
  useEffect(() => {
    document.title = `${title} | TrekMate`;
    document.querySelector('meta[name="description"]')?.setAttribute('content', description);
  }, [title, description]);
  return null;
}
