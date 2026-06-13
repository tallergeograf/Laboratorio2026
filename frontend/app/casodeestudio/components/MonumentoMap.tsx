'use client'

import { useEffect, useMemo } from 'react'
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import type { Monumento, NearestMonumento } from '@/lib/models/monumentos'

type MonumentoMapProps = {
  monumentos: Monumento[]
  nearestIds: Set<number>
  searchedLocation: [number, number] | null
  nearestMap: Map<number, NearestMonumento>
}

const MONTEVIDEO_CENTER: [number, number] = [-34.9011, -56.1645]

function FlyTo({ location }: { location: [number, number] | null }) {
  const map = useMap()
  useEffect(() => {
    if (location) map.flyTo(location, 15)
  }, [location, map])
  return null
}

function columnSvg(color: string, width: number, height: number): string {
  return `<svg xmlns="http://www.w3.org/2000/svg" width="${width}" height="${height}" viewBox="0 0 20 28">
    <rect x="1" y="24" width="18" height="3" rx="1" fill="${color}"/>
    <rect x="7" y="8" width="6" height="16" fill="${color}"/>
    <rect x="3" y="6" width="14" height="3" rx="1" fill="${color}"/>
    <polygon points="10,0 3,6 17,6" fill="${color}"/>
  </svg>`
}

export default function MonumentoMap({ monumentos, nearestIds, searchedLocation, nearestMap }: MonumentoMapProps) {
  const { defaultIcon, highlightIcon, searchPinIcon } = useMemo(() => ({
    defaultIcon: L.divIcon({
      className: '',
      html: columnSvg('#3b82f6', 20, 28),
      iconSize: [20, 28],
      iconAnchor: [10, 28],
    }),
    highlightIcon: L.divIcon({
      className: '',
      html: columnSvg('#ef4444', 24, 32),
      iconSize: [24, 32],
      iconAnchor: [12, 32],
    }),
    searchPinIcon: L.divIcon({
      className: '',
      html: '<div style="font-size:28px;line-height:1;filter:drop-shadow(0 1px 2px rgba(0,0,0,.5))">📍</div>',
      iconSize: [28, 28],
      iconAnchor: [14, 28],
    }),
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
        const nearestData = nearestMap.get(m.id)
        return (
          <Marker
            key={m.id}
            position={[m.latitud, m.longitud]}
            icon={isNearest ? highlightIcon : defaultIcon}
          >
            <Popup>
              <div className="text-sm space-y-0.5">
                <p className="font-semibold">{m.nombre}</p>
                {m.direccion && <p className="text-zinc-500">{m.direccion}</p>}
                {m.localidad && <p className="text-zinc-400">{m.localidad}</p>}
                {nearestData && (
                  <p className="text-blue-600 font-medium mt-1">
                    {nearestData.distanceMeters < 1000
                      ? `${Math.round(nearestData.distanceMeters)} m`
                      : `${(nearestData.distanceMeters / 1000).toFixed(2)} km`}
                  </p>
                )}
              </div>
            </Popup>
          </Marker>
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
