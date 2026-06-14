import L from 'leaflet'

export function columnSvg(color: string, size: number): string {
  return `<svg xmlns="http://www.w3.org/2000/svg" width="${size}" height="${size}" viewBox="0 0 1920 1792" style="display:block">
    <path fill="${color}" d="m960 0l960 384v128h-128q0 26-20.5 45t-48.5 19H197q-28 0-48.5-19T128 512H0V384zM256 640h256v768h128V640h256v768h128V640h256v768h128V640h256v768h59q28 0 48.5 19t20.5 45v64H128v-64q0-26 20.5-45t48.5-19h59zm1595 960q28 0 48.5 19t20.5 45v128H0v-128q0-26 20.5-45t48.5-19z"/>
  </svg>`
}

export function createDefaultIcon(): L.DivIcon {
  return L.divIcon({
    className: '',
    html: columnSvg('#C9952A', 20),
    iconSize: [20, 20],
    iconAnchor: [10, 20],
  })
}

export function createHighlightIcon(): L.DivIcon {
  return L.divIcon({
    className: '',
    html: columnSvg('#ef4444', 24),
    iconSize: [24, 24],
    iconAnchor: [12, 24],
  })
}

export function createSearchPinIcon(): L.DivIcon {
  return L.divIcon({
    className: '',
    html: `<svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="#10b981" stroke="white" stroke-width="1" stroke-linecap="round" stroke-linejoin="round" style="display:block;filter:drop-shadow(0 1px 3px rgba(0,0,0,.4))">
      <path d="M12 2a7 7 0 0 1 7 7c0 4.97-7 13-7 13S5 13.97 5 9a7 7 0 0 1 7-7z"/>
      <circle cx="12" cy="9" r="2.5" fill="white" stroke="none"/>
    </svg>`,
    iconSize: [32, 32],
    iconAnchor: [16, 32],
  })
}
