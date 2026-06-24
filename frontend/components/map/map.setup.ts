import maplibregl from 'maplibre-gl';
import {
  GEO_SOURCE_ID,
  LINES_SOURCE_ID,
  LINES_LAYER_ID,
  REAL_LAYER_ID,
  PROVIDER_LAYER_PREFIX,
  REAL_MARKER_COLOUR,
  PROVIDERS,
} from './map.constants';
import { geoJSONForPoints } from './map.utils';
import type { GeoJSONPointProperties, GeoPoint } from './map.types';

export function registerSources(map: maplibregl.Map) {
  if (!map.getSource(LINES_SOURCE_ID)) {
    map.addSource(LINES_SOURCE_ID, {
      type: 'geojson',
      data: { type: 'FeatureCollection', features: [] },
    });
  }
  if (!map.getSource(GEO_SOURCE_ID)) {
    map.addSource(GEO_SOURCE_ID, {
      type: 'geojson',
      data: geoJSONForPoints([]),
      cluster: true,
      clusterRadius: 50,
      clusterMaxZoom: 14,
    });
  }
}

export function registerLayers(map: maplibregl.Map) {
  // Lines first so points render on top
  if (!map.getLayer(LINES_LAYER_ID)) {
    map.addLayer({
      id: LINES_LAYER_ID,
      type: 'line',
      source: LINES_SOURCE_ID,
      paint: {
        'line-color': ['get', 'colour'],
        'line-width': 2,
        'line-opacity': 0.8,
        'line-dasharray': [3, 2],
      },
    });
  }

  if (!map.getLayer('clusters')) {
    map.addLayer({
      id: 'clusters',
      type: 'circle',
      source: GEO_SOURCE_ID,
      filter: ['has', 'point_count'],
      paint: {
        'circle-color': '#22C55E',
        'circle-radius': ['step', ['get', 'point_count'], 18, 10, 24, 30, 30],
        'circle-opacity': 0.8,
        'circle-stroke-color': '#000',
        'circle-stroke-width': 1,
      },
    });
  }

  if (!map.getLayer('cluster-count')) {
    map.addLayer({
      id: 'cluster-count',
      type: 'symbol',
      source: GEO_SOURCE_ID,
      filter: ['has', 'point_count'],
      layout: {
        'text-field': '{point_count_abbreviated}',
        'text-font': ['Open Sans Bold', 'Arial Unicode MS Bold'],
        'text-size': 12,
      },
      paint: { 'text-color': '#000' },
    });
  }

  if (!map.getLayer(REAL_LAYER_ID)) {
    map.addLayer({
      id: REAL_LAYER_ID,
      type: 'circle',
      source: GEO_SOURCE_ID,
      filter: ['all', ['==', ['get', 'pointType'], 'real'], ['!', ['has', 'point_count']]],
      paint: {
        'circle-radius': 7,
        'circle-color': REAL_MARKER_COLOUR,
        'circle-stroke-color': '#000',
        'circle-stroke-width': 1,
      },
    });
  }

  PROVIDERS.forEach((provider) => {
    const layerId = `${PROVIDER_LAYER_PREFIX}${provider.provider}`;
    if (!map.getLayer(layerId)) {
      map.addLayer({
        id: layerId,
        type: 'circle',
        source: GEO_SOURCE_ID,
        filter: [
          'all',
          ['==', ['get', 'pointType'], 'provider'],
          ['==', ['get', 'provider'], provider.provider],
          ['!', ['has', 'point_count']],
        ],
        paint: {
          'circle-radius': 5,
          'circle-color': provider.colour,
          'circle-stroke-color': '#000',
          'circle-stroke-width': 1,
        },
      });
    }
  });
}

function escape(value: string): string {
  return value
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;');
}

function buildPopupHTML(props: GeoJSONPointProperties): string {
  const address = escape(props.addressText ?? props.title ?? '');
  const base = 'color:#111;font-family:sans-serif;';

  if (props.pointType === 'provider' && props.errorMeters != null) {
    const provider = props.provider
      ? props.provider.charAt(0).toUpperCase() + props.provider.slice(1)
      : '';
    return `
      <div style="${base}min-width:230px;padding:4px 2px;">
        <div style="font-weight:700;font-size:13px;margin-bottom:3px;color:#111;">${address}</div>
        <div style="color:#777;font-size:11px;margin-bottom:10px;">${provider}</div>
        <div style="display:flex;justify-content:space-between;align-items:center;background:#f0f0f0;padding:6px 10px;border-radius:6px;margin-bottom:8px;">
          <span style="font-size:12px;color:#444;">Error</span>
          <strong style="font-size:14px;color:#111;">${props.errorMeters.toFixed(1)} m</strong>
        </div>
        <div style="font-size:10px;color:#666;line-height:1.8;">
          <div style="color:#666;">Real &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${props.realLat?.toFixed(5)}, ${props.realLon?.toFixed(5)}</div>
          <div style="color:#666;">Geocodif. &nbsp;${props.geocoderLat?.toFixed(5)}, ${props.geocoderLon?.toFixed(5)}</div>
        </div>
      </div>`;
  }

  return `<div style="${base}font-weight:600;padding:4px 2px;">${address}</div>`;
}

function buildMultiPopupHTML(points: GeoJSONPointProperties[]): string {
  const base = 'font-family:sans-serif;color:#111;';
  const items = points.map((p) => {
    const colour = p.colour ?? '#888';
    const providerLabel = p.pointType === 'real'
      ? 'Real'
      : p.provider ? p.provider.charAt(0).toUpperCase() + p.provider.slice(1) : '';
    const address = escape(p.addressText ?? p.title ?? '');
    const errorLine = p.pointType === 'provider' && p.errorMeters != null
      ? `<div style="font-size:10px;color:#888;margin-top:2px;">Error: ${p.errorMeters.toFixed(1)} m</div>`
      : '';
    return `
      <div style="display:flex;align-items:flex-start;gap:8px;padding:6px 8px;background:#f2f2f2;border-radius:6px;">
        <span style="flex-shrink:0;width:8px;height:8px;border-radius:50%;margin-top:3px;background:${colour};"></span>
        <div style="flex:1;min-width:0;">
          <div style="font-size:10px;font-weight:700;color:#666;margin-bottom:1px;text-transform:uppercase;letter-spacing:0.4px;">${providerLabel}</div>
          <div style="font-size:11px;color:#222;line-height:1.35;">${address}</div>
          ${errorLine}
        </div>
      </div>`;
  }).join('');

  return `
    <div style="${base}min-width:240px;padding:4px 2px;">
      <div style="display:flex;align-items:center;gap:7px;margin-bottom:10px;">
        <span style="background:#444;color:#fff;font-size:10px;font-weight:700;padding:1px 8px;border-radius:10px;">${points.length}</span>
        <span style="font-size:12px;font-weight:600;color:#333;">puntos en esta ubicación</span>
      </div>
      <div style="display:flex;flex-direction:column;gap:5px;">${items}</div>
    </div>`;
}

function clearLine(map: maplibregl.Map) {
  try {
    const src = map.getSource(LINES_SOURCE_ID) as maplibregl.GeoJSONSource | undefined;
    if (src) src.setData({ type: 'FeatureCollection', features: [] });
  } catch { /* map was removed */ }
}

type LineSource = {
  realLat?: number | null;
  realLon?: number | null;
  geocoderLat?: number | null;
  geocoderLon?: number | null;
  colour: string;
};

function drawLines(map: maplibregl.Map, points: LineSource[]) {
  const features = points
    .filter(p => p.realLat != null && p.realLon != null && p.geocoderLat != null && p.geocoderLon != null)
    .map(p => ({
      type: 'Feature' as const,
      geometry: {
        type: 'LineString' as const,
        coordinates: [[p.realLon!, p.realLat!], [p.geocoderLon!, p.geocoderLat!]],
      },
      properties: { colour: p.colour },
    }));

  if (features.length === 0) return;
  try {
    const src = map.getSource(LINES_SOURCE_ID) as maplibregl.GeoJSONSource | undefined;
    if (src) src.setData({ type: 'FeatureCollection', features });
  } catch { /* map was removed */ }
}

function isPointGeometry(geometry: GeoJSON.Geometry | null | undefined): geometry is GeoJSON.Point {
  return geometry?.type === 'Point' && Array.isArray((geometry as GeoJSON.Point).coordinates);
}

export function registerInteractions(map: maplibregl.Map, getPoints: () => GeoPoint[]) {
  const popup = new maplibregl.Popup({ closeButton: true, closeOnClick: true, maxWidth: '280px' });
  const layerIds = [REAL_LAYER_ID, ...PROVIDERS.map((p) => `${PROVIDER_LAYER_PREFIX}${p.provider}`)];

  map.on('click', 'clusters', async (event: maplibregl.MapLayerMouseEvent) => {
    if (!event.features?.length) return;
    const cluster = event.features[0];
    const clusterId = cluster.properties?.cluster_id as number | undefined;
    const source = map.getSource(GEO_SOURCE_ID) as maplibregl.GeoJSONSource;
    if (clusterId == null || !source) return;
    const zoom = await source.getClusterExpansionZoom(clusterId);
    if (zoom == null) return;
    const coordinates = (cluster.geometry as GeoJSON.Point).coordinates as [number, number];
    map.easeTo({ center: coordinates, zoom });
  });

  layerIds.forEach((layerId) => {
    map.on('click', layerId, (event: maplibregl.MapLayerMouseEvent) => {
      const features = map.queryRenderedFeatures(event.point, { layers: layerIds }) ?? [];
      const valid = features.filter((f) => f.geometry?.type === 'Point') as maplibregl.MapGeoJSONFeature[];
      if (valid.length === 0) return;

      const firstGeom = valid[0].geometry;
      if (!isPointGeometry(firstGeom)) return;
      const [lng, lat] = firstGeom.coordinates;

      const EPS = 1e-5;
      const sameLocation = valid.filter((f) => {
        const coords = isPointGeometry(f.geometry) ? f.geometry.coordinates : null;
        return Array.isArray(coords)
          && Math.abs(Number(coords[0]) - lng) < EPS
          && Math.abs(Number(coords[1]) - lat) < EPS;
      });

      const firstProps = sameLocation[0].properties as GeoJSONPointProperties;

      clearLine(map);

      const providerPoints = sameLocation
        .map(f => f.properties as GeoJSONPointProperties)
        .filter(p => p.pointType === 'provider');

      if (providerPoints.length > 0) {
        // Provider point(s) clicked → draw line(s) to their real location
        drawLines(map, providerPoints);
      } else {
        // Real point clicked → search all loaded points (bypasses viewport and cluster limits)
        const matchingProviders = getPoints().filter(p =>
          p.pointType === 'provider'
          && Math.abs((p.realLat ?? NaN) - lat) < EPS
          && Math.abs((p.realLon ?? NaN) - lng) < EPS
        );
        drawLines(map, matchingProviders);
      }

      const html = sameLocation.length > 1
        ? buildMultiPopupHTML(sameLocation.map((f) => f.properties as GeoJSONPointProperties))
        : buildPopupHTML(firstProps);

      popup.setLngLat([lng, lat]).setHTML(html).addTo(map);
    });

    map.on('mouseenter', layerId, () => { map.getCanvas().style.cursor = 'pointer'; });
    map.on('mouseleave', layerId, () => { map.getCanvas().style.cursor = ''; });
  });

  // Clear line when popup is dismissed without clicking another point
  popup.on('close', () => clearLine(map));

  map.on('mouseenter', 'clusters', () => { map.getCanvas().style.cursor = 'pointer'; });
  map.on('mouseleave', 'clusters', () => { map.getCanvas().style.cursor = ''; });
}
