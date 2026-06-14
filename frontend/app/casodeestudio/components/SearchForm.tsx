import { PROVIDERS, type Provider, type SearchMode } from '@/app/casodeestudio/types'

type SearchFormProps = {
  address: string
  onAddressChange: (v: string) => void
  provider: Provider
  onProviderChange: (v: Provider) => void
  searchMode: SearchMode
  limit: number
  onLimitChange: (v: number) => void
  radius: number
  onRadiusChange: (v: number) => void
  loading: boolean
  onSubmit: (e: React.FormEvent) => void
}

export default function SearchForm({
  address,
  onAddressChange,
  provider,
  onProviderChange,
  searchMode,
  limit,
  onLimitChange,
  radius,
  onRadiusChange,
  loading,
  onSubmit,
}: SearchFormProps) {
  return (
    <form onSubmit={onSubmit} className="flex flex-col sm:flex-row gap-2">
      <input
        type="text"
        value={address}
        onChange={(e) => onAddressChange(e.target.value)}
        placeholder="Ingresá una dirección en Montevideo"
        className="flex-1 rounded-md border border-zinc-300 dark:border-zinc-700 bg-white dark:bg-zinc-900 px-3 py-2 text-sm shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        disabled={loading}
      />

      {searchMode === 'nearest' ? (
        <div className="flex items-center gap-1">
          <label className="text-sm text-zinc-500 dark:text-zinc-400 whitespace-nowrap">Cantidad</label>
          <input
            type="number"
            value={limit}
            onChange={(e) => onLimitChange(Math.max(1, Math.min(50, Number(e.target.value))))}
            min={1}
            max={50}
            className="w-20 rounded-md border border-zinc-300 dark:border-zinc-700 bg-white dark:bg-zinc-900 px-3 py-2 text-sm shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
            disabled={loading}
          />
        </div>
      ) : (
        <div className="flex items-center gap-1">
          <label className="text-sm text-zinc-500 dark:text-zinc-400 whitespace-nowrap">Distancia (m)</label>
          <input
            type="number"
            value={radius}
            onChange={(e) => onRadiusChange(Math.max(1, Math.min(5000, Number(e.target.value))))}
            min={1}
            max={5000}
            className="w-24 rounded-md border border-zinc-300 dark:border-zinc-700 bg-white dark:bg-zinc-900 px-3 py-2 text-sm shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
            disabled={loading}
          />
        </div>
      )}

      <select
        value={provider}
        onChange={(e) => onProviderChange(e.target.value as Provider)}
        className="rounded-md border border-zinc-300 dark:border-zinc-700 bg-white dark:bg-zinc-900 px-3 py-2 text-sm shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 cursor-pointer"
        disabled={loading}
      >
        {PROVIDERS.map((p) => (
          <option key={p.value} value={p.value}>
            {p.label}
          </option>
        ))}
      </select>

      <button
        type="submit"
        disabled={loading}
        className="rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-50 cursor-pointer disabled:cursor-not-allowed transition-colors"
      >
        {loading ? 'Buscando…' : 'Buscar'}
      </button>
    </form>
  )
}
