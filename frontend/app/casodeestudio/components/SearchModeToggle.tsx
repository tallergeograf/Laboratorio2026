import type { SearchMode } from '@/app/casodeestudio/types'

type SearchModeToggleProps = {
  value: SearchMode
  onChange: (mode: SearchMode) => void
}

export default function SearchModeToggle({ value, onChange }: SearchModeToggleProps) {
  return (
    <div className="flex rounded-md border border-zinc-300 dark:border-zinc-700 w-fit overflow-hidden text-sm">
      <button
        type="button"
        onClick={() => onChange('nearest')}
        className={`px-4 py-2 cursor-pointer transition-colors ${
          value === 'nearest'
            ? 'bg-blue-600 text-white'
            : 'bg-white dark:bg-zinc-900 text-zinc-600 dark:text-zinc-400 hover:bg-zinc-50 dark:hover:bg-zinc-800'
        }`}
      >
        Los más cercanos
      </button>
      <button
        type="button"
        onClick={() => onChange('radius')}
        className={`px-4 py-2 border-l border-zinc-300 dark:border-zinc-700 cursor-pointer transition-colors ${
          value === 'radius'
            ? 'bg-blue-600 text-white'
            : 'bg-white dark:bg-zinc-900 text-zinc-600 dark:text-zinc-400 hover:bg-zinc-50 dark:hover:bg-zinc-800'
        }`}
      >
        Por distancia
      </button>
    </div>
  )
}
