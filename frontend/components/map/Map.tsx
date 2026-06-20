'use client';

import { useEffect, useMemo, useRef } from 'react';
import type maplibregl from 'maplibre-gl';
import 'maplibre-gl/dist/maplibre-gl.css';

import { createMap } from './map.factory';
import { registerSources, registerLayers, registerInteractions } from './map.setup';
import { geoJSONForPoints } from './map.utils';
import { GEO_SOURCE_ID, REAL_MARKER_COLOUR, PROVIDERS } from './map.constants';
import type { GeoPoint } from './map.types';
import { useGeoPoints } from '@/hooks/useGeoPoints';
import { useFilters } from '@/app/benchmark/FiltersContext';

export default function Map() {
  const mapContainer = useRef<HTMLDivElement | null>(null);
  const mapRef = useRef<maplibregl.Map | null>(null);
  const filteredPointsRef = useRef<GeoPoint[]>([]);

  const filters = useFilters();
  const { points, stats, loading, error } = useGeoPoints(filters);

  const selectedProviders = useMemo(
    () => new Set(filters.providers.map((p) => p.toLowerCase())),
    [filters.providers]
  );

  const filteredPoints = useMemo(
    () =>
      points.filter((point) => {
        if (point.pointType === 'real') return true;
        if (selectedProviders.size === 0) return true;
        return point.provider ? selectedProviders.has(point.provider) : false;
      }),
    [points, selectedProviders]
  );

  // Init map once
  useEffect(() => {
    if (!mapContainer.current || mapRef.current) return;
    const map = createMap(mapContainer.current);
    map.on('load', () => {
      registerSources(map);
      registerLayers(map);
      registerInteractions(map, () => filteredPointsRef.current);
      const src = map.getSource(GEO_SOURCE_ID) as maplibregl.GeoJSONSource | undefined;
      if (src) src.setData(geoJSONForPoints([]));
    });
    mapRef.current = map;
    return () => {
      map.remove();
      mapRef.current = null;
    };
  }, []);

  // Update points when filtered data changes
  useEffect(() => {
    filteredPointsRef.current = filteredPoints;
    const map = mapRef.current;
    if (!map) return;
    const update = () => {
      const src = map.getSource(GEO_SOURCE_ID) as maplibregl.GeoJSONSource | undefined;
      if (src) src.setData(geoJSONForPoints(filteredPoints));
    };
    if (map.isStyleLoaded()) update();
    else map.once('load', update);
  }, [filteredPoints]);

  return (
    <div className="w-full h-screen flex flex-col">
      <div className="relative flex-1">
        <div ref={mapContainer} className="w-full h-full" />

        {loading && (
          <div className="absolute inset-0 flex items-center justify-center bg-black/50 z-10 pointer-events-none">
            <span className="text-white/70 text-sm tracking-wide">Cargando puntos…</span>
          </div>
        )}

        {error && !loading && (
          <div className="absolute top-4 left-1/2 -translate-x-1/2 z-10 bg-red-900/80 text-red-200 text-xs px-4 py-2 rounded-lg shadow-lg">
            {error.message}
          </div>
        )}

        <aside
          aria-label="Leyenda del mapa"
          className="absolute left-4 bottom-4 z-20 max-w-full bg-black/70 text-white p-3 rounded-md text-sm shadow-lg"
        >
          <div className="mb-2 font-semibold text-xs text-white/50 uppercase tracking-wider">Leyenda</div>
          <div className="flex flex-wrap gap-4 items-center">
            <div className="flex items-center gap-2">
              <span className="inline-block h-3 w-3 rounded-full" style={{ backgroundColor: REAL_MARKER_COLOUR }} />
              <span className="text-white/80">Posiciones reales</span>
            </div>
            {PROVIDERS.map((provider) => {
              const count = stats.find((s) => s.provider === provider.provider)?.sampleSize;
              return (
                <div key={provider.provider} className="flex items-center gap-2">
                  <span className="inline-block h-3 w-3 rounded-full" style={{ backgroundColor: provider.colour }} />
                  <span className="text-white/80">
                    {provider.label}
                    {count != null && (
                      <span className="text-white/40 text-xs ml-1">({count})</span>
                    )}
                  </span>
                </div>
              );
            })}
          </div>
        </aside>
      </div>
    </div>
  );
}
