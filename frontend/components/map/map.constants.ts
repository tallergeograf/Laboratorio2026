import type { StyleSpecification } from 'maplibre-gl';

export const PROVIDERS = [
  { provider: 'sudir', label: 'Sudir', colour: '#3B82F6' },
  { provider: 'photon', label: 'Photon', colour: '#F97316' },
  { provider: 'nominatim', label: 'Nominatim', colour: '#A78BFA' },
] as const;

export const providerColourByName: Record<string, string> = {
  sudir: '#3B82F6',
  photon: '#F97316',
  nominatim: '#A78BFA',
};

export const GEO_SOURCE_ID = 'geo-points-source';
export const LINES_SOURCE_ID = 'geo-lines-source';
export const REAL_LAYER_ID = 'real-points-layer';
export const LINES_LAYER_ID = 'geo-lines-layer';
export const PROVIDER_LAYER_PREFIX = 'provider-points-';
export const REAL_MARKER_COLOUR = '#33FFF6';

export const DARK_STYLE_URL = 'https://basemaps.cartocdn.com/gl/dark-matter-gl-style/style.json';

export const OSM_STYLE: StyleSpecification = {
  version: 8 as const,
  sources: {
    osmTiles: {
      type: 'raster',
      tiles: ['https://a.tile.openstreetmap.org/{z}/{x}/{y}.png'],
      tileSize: 256,
    },
  },
  layers: [
    {
      id: 'osm-tiles',
      type: 'raster',
      source: 'osmTiles',
    },
  ],
};

