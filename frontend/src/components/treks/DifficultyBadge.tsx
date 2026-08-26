import type { Difficulty } from '../../types/trek';

const styles: Record<Difficulty, string> = {
  EASY: 'bg-emerald-100 text-emerald-800 dark:bg-emerald-950 dark:text-emerald-300',
  MODERATE: 'bg-amber-100 text-amber-800 dark:bg-amber-950 dark:text-amber-300',
  CHALLENGING: 'bg-orange-100 text-orange-800 dark:bg-orange-950 dark:text-orange-300',
  DIFFICULT: 'bg-rose-100 text-rose-800 dark:bg-rose-950 dark:text-rose-300',
};

export function DifficultyBadge({ difficulty }: { difficulty: Difficulty }) {
  return <span className={`rounded-full px-2.5 py-1 text-xs font-bold tracking-wide ${styles[difficulty]}`}>{difficulty}</span>;
}
