'use client'

import dynamic from 'next/dynamic'
import type { Monumento } from '@/lib/models/monumentos'
import { useMonumentoSearch } from '@/hooks/useMonumentoSearch'
import { useWikipedia } from '@/hooks/useWikipedia'
import SearchModeToggle from './SearchModeToggle'
import SearchForm from './SearchForm'
import ResultLabel from './ResultLabel'
import MonumentoModal from './MonumentoModal'

const MonumentoMap = dynamic(() => import('./MonumentoMap'), { ssr: false })

type Props = {
  initialMonumentos: Monumento[]
}

export default function CasoDeEstudioClient({ initialMonumentos }: Props) {
  const {
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
  } = useMonumentoSearch()

  const { selectedMonumento, wikiData, wikiLoading, handleMarkerClick, clearSelection } = useWikipedia()

  return (
    <div className="flex flex-col gap-4">
      <SearchModeToggle value={searchMode} onChange={handleModeChange} />

      <SearchForm
        address={address}
        onAddressChange={setAddress}
        provider={provider}
        onProviderChange={setProvider}
        searchMode={searchMode}
        limit={limit}
        onLimitChange={setLimit}
        radius={radius}
        onRadiusChange={setRadius}
        loading={loading}
        onSubmit={handleSearch}
      />

      {error && (
        <p className="text-sm text-red-600 dark:text-red-400">{error}</p>
      )}

      <ResultLabel count={nearestIds.size} searchMode={searchMode} radius={radius} />

      <MonumentoMap
        monumentos={initialMonumentos}
        nearestIds={nearestIds}
        searchedLocation={searchedLocation}
        nearestMap={nearestMap}
        onMarkerClick={handleMarkerClick}
      />

      {selectedMonumento && (
        <MonumentoModal
          monumento={selectedMonumento}
          wikiData={wikiData}
          loading={wikiLoading}
          onClose={clearSelection}
        />
      )}
    </div>
  )
}
