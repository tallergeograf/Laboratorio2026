import { useState, useCallback } from 'react'
import type { Monumento } from '@/lib/models/monumentos'
import { defaultProvider, type MonumentInfo } from '@/lib/monument-info'

export function useMonumentInfo() {
  const [selectedMonumento, setSelectedMonumento] = useState<Monumento | null>(null)
  const [info, setInfo] = useState<MonumentInfo | null>(null)
  const [infoLoading, setInfoLoading] = useState(false)

  const handleMarkerClick = useCallback(async (m: Monumento) => {
    setSelectedMonumento(m)
    setInfo(null)
    setInfoLoading(true)
    const data = await defaultProvider.getInfo(m.nombre)
    setInfo(data)
    setInfoLoading(false)
  }, [])

  const clearSelection = useCallback(() => {
    setSelectedMonumento(null)
  }, [])

  return {
    selectedMonumento,
    info,
    infoLoading,
    handleMarkerClick,
    clearSelection,
  }
}
