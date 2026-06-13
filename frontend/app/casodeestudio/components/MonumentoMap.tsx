'use client'

import { useEffect, useMemo } from 'react'
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet'
import 'leaflet/dist/leaflet.css'
import type { Monumento, NearestMonumento } from '@/lib/models/monumentos'
import { createDefaultIcon, createHighlightIcon, createSearchPinIcon } from '@/lib/icons/monument-icons'

type MonumentoMapProps = {
  monumentos: Monumento[]
  nearestIds: Set<number>
  searchedLocation: [number, number] | null
  nearestMap: Map<number, NearestMonumento>
  onMarkerClick: (monumento: Monumento) => void
}

const MONTEVIDEO_CENTER: [number, number] = [-34.9011, -56.1645]

function FlyTo({ location }: { location: [number, number] | null }) {
  const map = useMap()
  useEffect(() => {
    if (location) map.flyTo(location, 17)
  }, [location, map])
  return null
}

export default function MonumentoMap({ monumentos, nearestIds, searchedLocation, nearestMap, onMarkerClick }: MonumentoMapProps) {
  const { defaultIcon, highlightIcon, searchPinIcon } = useMemo(() => ({
    defaultIcon: createDefaultIcon(),
    highlightIcon: createHighlightIcon(),
    searchPinIcon: createSearchPinIcon(),
  }), [])

  return (
    <MapContainer
      center={MONTEVIDEO_CENTER}
      zoom={13}
      style={{ height: '520px', width: '100%', borderRadius: '0.5rem' }}
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />

      <FlyTo location={searchedLocation} />

      {monumentos.map((m) => {
        const isNearest = nearestIds.has(m.id)
        return (
          <Marker
            key={m.id}
            position={[m.latitud, m.longitud]}
            icon={isNearest ? highlightIcon : defaultIcon}
            eventHandlers={{ click: () => onMarkerClick(m) }}
          />
        )
      })}

      {searchedLocation && (
        <Marker position={searchedLocation} icon={searchPinIcon}>
          <Popup>
            <p className="text-sm font-medium">Dirección buscada</p>
          </Popup>
        </Marker>
      )}
    </MapContainer>
  )
}
