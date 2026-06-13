import { useState } from 'react'
import type { NearestMonumento } from '@/lib/models/monumentos'
import { getNearestMonumentos, getMonumentosWithinRadius } from '@/lib/backend-api/monumentos'
import type { Provider, SearchMode } from '@/app/casodeestudio/types'

export function useMonumentoSearch() {
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
      const results =
        searchMode === 'nearest'
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

  return {
    address,
    setAddress,
    provider,
    setProvider,
    searchMode,
    limit,
    setLimit,
    radius,
    setRadius,
    nearestIds,
    nearestMap,
    searchedLocation,
    loading,
    error,
    handleSearch,
    handleModeChange,
  }
}
