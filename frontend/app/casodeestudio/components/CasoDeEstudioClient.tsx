'use client'

import dynamic from 'next/dynamic'
import { useState } from 'react'
import type { Monumento, NearestMonumento } from '@/lib/models/monumentos'
import { getNearestMonumentos, getMonumentosWithinRadius } from '@/lib/backend-api/monumentos'

const MonumentoMap = dynamic(() => import('./MonumentoMap'), { ssr: false })

const PROVIDERS = [
  { value: 'NOMINATIM', label: 'Nominatim' },
  { value: 'PHOTON', label: 'Photon' },
  { value: 'SUDIR', label: 'Sudir (IMM)' },
] as const

type Provider = (typeof PROVIDERS)[number]['value']
type SearchMode = 'nearest' | 'radius'

type Props = {
  initialMonumentos: Monumento[]
}

export default function CasoDeEstudioClient({ initialMonumentos }: Props) {
  const [address, setAddress] = useState('')
  const [provider, setProvider] = useState<Provider>('NOMINATIM')
  const [searchMode, setSearchMode] = useState<SearchMode>('nearest')
  const [limit, setLimit] = useState(10)
  const [radius, setRadius] = useState(500)
  const [nearestIds, setNearestIds] = useState<Set<number>>(new Set())
  const [nearestMap, setNearestMap] = useState<Map<number, NearestMonumento>>(new Map())
  const [searchedLocation, setSearchedLocation] = useState<[number, number] | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  function handleModeChange(mode: SearchMode) {
    setSearchMode(mode)
    setNearestIds(new Set())
    setNearestMap(new Map())
    setSearchedLocation(null)
  }

  async function handleSearch(e: React.FormEvent) {
    e.preventDefault()
    if (!address.trim()) {
      setError('Ingresá una dirección para buscar.')
      return
    }
    setLoading(true)
    setError(null)
    try {
      const results = searchMode === 'nearest'
        ? await getNearestMonumentos({ address: address.trim(), provider, limit })
        : await getMonumentosWithinRadius({ address: address.trim(), provider, radius })

      const ids = new Set(results.map((r) => r.id))
      const map = new Map(results.map((r) => [r.id, r]))
      setNearestIds(ids)
      setNearestMap(map)
      const first = results[0]
      if (first) setSearchedLocation([first.lat, first.lon])
      else setSearchedLocation(null)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al buscar. Intentá de nuevo.')
    } finally {
      setLoading(false)
    }
  }

  const resultLabel = nearestIds.size > 0
    ? searchMode === 'nearest'
      ? `${nearestIds.size} monumento${nearestIds.size !== 1 ? 's' : ''} más cercano${nearestIds.size !== 1 ? 's' : ''} encontrado${nearestIds.size !== 1 ? 's' : ''}`
      : `${nearestIds.size} monumento${nearestIds.size !== 1 ? 's' : ''} encontrado${nearestIds.size !== 1 ? 's' : ''} a menos de ${radius} m de tu dirección`
    : null

  return (
    <div className="flex flex-col gap-4">
      {/* Search mode toggle */}
      <div className="flex rounded-md border border-zinc-300 dark:border-zinc-700 w-fit overflow-hidden text-sm">
        <button
          type="button"
          onClick={() => handleModeChange('nearest')}
          className={`px-4 py-2 transition-colors ${
            searchMode === 'nearest'
              ? 'bg-blue-600 text-white'
              : 'bg-white dark:bg-zinc-900 text-zinc-600 dark:text-zinc-400 hover:bg-zinc-50 dark:hover:bg-zinc-800'
          }`}
        >
          Los más cercanos
        </button>
        <button
          type="button"
          onClick={() => handleModeChange('radius')}
          className={`px-4 py-2 border-l border-zinc-300 dark:border-zinc-700 transition-colors ${
            searchMode === 'radius'
              ? 'bg-blue-600 text-white'
              : 'bg-white dark:bg-zinc-900 text-zinc-600 dark:text-zinc-400 hover:bg-zinc-50 dark:hover:bg-zinc-800'
          }`}
        >
          Por distancia
        </button>
      </div>

      <form onSubmit={handleSearch} className="flex flex-col sm:flex-row gap-2">
        <input
          type="text"
          value={address}
          onChange={(e) => setAddress(e.target.value)}
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
              onChange={(e) => setLimit(Math.max(1, Math.min(50, Number(e.target.value))))}
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
              onChange={(e) => setRadius(Math.max(1, Math.min(5000, Number(e.target.value))))}
              min={1}
              max={5000}
              className="w-24 rounded-md border border-zinc-300 dark:border-zinc-700 bg-white dark:bg-zinc-900 px-3 py-2 text-sm shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              disabled={loading}
            />
          </div>
        )}

        <select
          value={provider}
          onChange={(e) => setProvider(e.target.value as Provider)}
          className="rounded-md border border-zinc-300 dark:border-zinc-700 bg-white dark:bg-zinc-900 px-3 py-2 text-sm shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
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
          className="rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
        >
          {loading ? 'Buscando…' : 'Buscar'}
        </button>
      </form>

      {error && (
        <p className="text-sm text-red-600 dark:text-red-400">{error}</p>
      )}

      {resultLabel && (
        <p className="text-sm text-zinc-500">{resultLabel} — marcados en rojo</p>
      )}

      <MonumentoMap
        monumentos={initialMonumentos}
        nearestIds={nearestIds}
        searchedLocation={searchedLocation}
        nearestMap={nearestMap}
      />
    </div>
  )
}
