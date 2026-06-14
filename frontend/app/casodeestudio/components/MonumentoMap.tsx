'use client'
import { memo, useEffect, useMemo } from 'react'
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet'
import MarkerClusterGroup from 'react-leaflet-markercluster'
import 'leaflet/dist/leaflet.css'
import L from 'leaflet'
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

function clusterSvg(count: number, size: number, isRed: boolean = false): string {
  const r  = size / 2
  const cx = r
  const cy = r

  const primary   = isRed ? '#C0392B' : '#8B6914'
  const light     = isRed ? '#E74C3C' : '#C9952A'
  const lighter   = isRed ? '#F1948A' : '#E8C96A'
  const ringColor = isRed ? '#922B21' : '#6B5010'
  const glowColor = isRed ? 'rgba(231,76,60,0.5)' : 'rgba(201,149,42,0.5)'

  const gradId = `g${count}${size}${isRed ? 'r' : 'g'}`
  const glowId = `f${count}${size}${isRed ? 'r' : 'g'}`

  const ts       = (size * 0.36) / 32
  const tx       = (size - size * 0.36) / 2
  const ty       = size * 0.055
  const fontSize = size < 44 ? size * 0.29 : size * 0.27
  const textY    = size * 0.905

  return `<svg xmlns="http://www.w3.org/2000/svg" width="${size}" height="${size}" viewBox="0 0 ${size} ${size}" style="display:block;overflow:visible">
  <defs>
    <radialGradient id="${gradId}" cx="38%" cy="32%" r="65%">
      <stop offset="0%"   stop-color="${lighter}"/>
      <stop offset="45%"  stop-color="${light}"/>
      <stop offset="100%" stop-color="${primary}"/>
    </radialGradient>
    <filter id="${glowId}" x="-40%" y="-40%" width="180%" height="180%">
      <feGaussianBlur stdDeviation="${size * 0.12}" result="blur"/>
      <feFlood flood-color="${glowColor}" result="color"/>
      <feComposite in="color" in2="blur" operator="in" result="glow"/>
      <feMerge><feMergeNode in="glow"/><feMergeNode in="SourceGraphic"/></feMerge>
    </filter>
  </defs>
  <circle cx="${cx}" cy="${cy}" r="${r - 1}" fill="${light}" opacity="0.25" filter="url(#${glowId})"/>
  <circle cx="${cx}" cy="${cy}" r="${r - 0.5}" fill="none" stroke="${ringColor}" stroke-width="1.5" opacity="0.6"/>
  <circle cx="${cx}" cy="${cy}" r="${r - 2}" fill="url(#${gradId})"/>
  <circle cx="${cx}" cy="${cy}" r="${r - 2}" fill="none" stroke="rgba(255,255,255,0.35)" stroke-width="1.2"/>
  <ellipse cx="${cx - r * 0.18}" cy="${cy - r * 0.22}" rx="${r * 0.38}" ry="${r * 0.22}" fill="white" opacity="0.22" transform="rotate(-30 ${cx} ${cy})"/>
  <g transform="translate(${tx}, ${ty}) scale(${ts})">
    <polygon points="16,1 29,8.5 3,8.5" fill="white" opacity="0.96"/>
    <rect x="3.5" y="8.5" width="25" height="2.8" fill="white" opacity="0.96"/>
    <rect x="5"    y="11.5" width="3.2" height="9.5" rx="0.8" fill="white" opacity="0.96"/>
    <rect x="10.2" y="11.5" width="3.2" height="9.5" rx="0.8" fill="white" opacity="0.96"/>
    <rect x="15.4" y="11.5" width="3.2" height="9.5" rx="0.8" fill="white" opacity="0.96"/>
    <rect x="20.6" y="11.5" width="3.2" height="9.5" rx="0.8" fill="white" opacity="0.96"/>
    <rect x="3" y="21" width="26" height="2.5" rx="0.5" fill="white" opacity="0.96"/>
  </g>
  <text x="${cx + 0.5}" y="${textY + 0.8}" text-anchor="middle" font-size="${fontSize}" font-weight="800" font-family="system-ui,-apple-system,sans-serif" fill="rgba(0,0,0,0.35)" letter-spacing="-0.5">${count}</text>
  <text x="${cx}" y="${textY}" text-anchor="middle" font-size="${fontSize}" font-weight="800" font-family="system-ui,-apple-system,sans-serif" fill="white" letter-spacing="-0.5">${count}</text>
</svg>`
}

function createClusterIcon(cluster: { getChildCount: () => number; getAllChildMarkers: () => L.Marker[] }): L.DivIcon {
  const count      = cluster.getChildCount()
  const hasNearest = cluster.getAllChildMarkers().some((m: L.Marker) => m.options.alt === 'nearest')
  const size       = count < 10 ? 42 : count < 50 ? 50 : 60
  return L.divIcon({
    className: '',
    html: clusterSvg(count, size, hasNearest),
    iconSize: [size, size],
    iconAnchor: [size / 2, size / 2],
  })
}

function FlyTo({ location }: { location: [number, number] | null }) {
  const map = useMap()
  useEffect(() => {
    if (location) map.flyTo(location, 17)
  }, [location, map])
  return null
}

const MonumentoMap = memo(function MonumentoMap({ monumentos, nearestIds, searchedLocation, onMarkerClick }: MonumentoMapProps) {
  const { defaultIcon, highlightIcon, searchPinIcon } = useMemo(() => ({
    defaultIcon:   createDefaultIcon(),
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

      <MarkerClusterGroup
        key={[...nearestIds].join(',')}
        iconCreateFunction={createClusterIcon}
        maxClusterRadius={60}
        spiderfyOnMaxZoom={true}
        showCoverageOnHover={false}
        zoomToBoundsOnClick={true}
      >
        {monumentos.map((m) => {
          const isNearest = nearestIds.has(m.id)
          return (
            <Marker
              key={m.id}
              position={[m.latitud, m.longitud]}
              icon={isNearest ? highlightIcon : defaultIcon}
              alt={isNearest ? 'nearest' : 'default'}
              eventHandlers={{ click: () => onMarkerClick(m) }}
            />
          )
        })}
      </MarkerClusterGroup>

      {searchedLocation && (
        <Marker position={searchedLocation} icon={searchPinIcon}>
          <Popup>
            <p className="text-sm font-medium">Dirección buscada</p>
          </Popup>
        </Marker>
      )}
    </MapContainer>
  )
})

export default MonumentoMap
