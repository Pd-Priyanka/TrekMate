import { FormEvent, useState } from 'react';

export function SearchBar({ initialValue = '', onSearch }: { initialValue?: string; onSearch: (keyword: string) => void }) {
  const [value, setValue] = useState(initialValue);
  const submit = (event: FormEvent) => { event.preventDefault(); onSearch(value.trim()); };
  return <form onSubmit={submit} className="flex rounded-xl bg-white p-1.5 shadow-lg ring-1 ring-slate-200 dark:bg-slate-900 dark:ring-slate-700"><input value={value} onChange={(event) => setValue(event.target.value)} className="min-w-0 flex-1 bg-transparent px-3 py-2 text-sm text-slate-900 outline-none placeholder:text-slate-400 dark:text-slate-100" placeholder="Search by trek, place, or state..." aria-label="Search treks"/><button className="rounded-lg bg-emerald-600 px-4 py-2 text-sm font-semibold text-white hover:bg-emerald-500">Search</button></form>;
}
