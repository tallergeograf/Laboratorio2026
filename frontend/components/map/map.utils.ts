import type { FeatureCollection, Point } from 'geojson';
import type { GeoPoint, GeoJSONPointProperties, GeoJSONPointFeature } from './map.types';

export function geoJSONForPoints(points: GeoPoint[]): FeatureCollection<Point, GeoJSONPointProperties> {
  const features: GeoJSONPointFeature[] = points.map((point) => ({
    type: 'Feature',
    geometry: {
      type: 'Point',
      coordinates: [point.longitude, point.latitude],
    },
    properties: {
      id: point.id,
      title: point.title,
      colour: point.colour,
      pointType: point.pointType,
      provider: (point.provider ?? null) as GeoJSONPointProperties['provider'],
      errorMeters: point.errorMeters ?? null,
      addressText: point.addressText ?? null,
      realLat: point.realLat ?? null,
      realLon: point.realLon ?? null,
      geocoderLat: point.geocoderLat ?? null,
      geocoderLon: point.geocoderLon ?? null,
    },
  }));

  return { type: 'FeatureCollection', features } as FeatureCollection<Point, GeoJSONPointProperties>;
}
