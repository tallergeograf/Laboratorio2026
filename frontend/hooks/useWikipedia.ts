import { useState } from 'react'
import type { Monumento } from '@/lib/models/monumentos'
import { fetchWikipediaSummary, type WikipediaSummary } from '@/lib/wikipedia'

export function useWikipedia() {
  const [selectedMonumento, setSelectedMonumento] = useState<Monumento | null>(null)
  const [wikiData, setWikiData] = useState<WikipediaSummary | null>(null)
  const [wikiLoading, setWikiLoading] = useState(false)

  async function handleMarkerClick(m: Monumento) {
    setSelectedMonumento(m)
    setWikiData(null)
    setWikiLoading(true)
    const data = await fetchWikipediaSummary(m.nombre)
    setWikiData(data)
    setWikiLoading(false)
  }

  function clearSelection() {
    setSelectedMonumento(null)
  }

  return {
    selectedMonumento,
    wikiData,
    wikiLoading,
    handleMarkerClick,
    clearSelection,
  }
}
