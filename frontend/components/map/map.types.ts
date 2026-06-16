import type { FeatureCollection, Feature, Point, LineString } from 'geojson';
import { PROVIDERS } from './map.constants';

export type ProviderName = (typeof PROVIDERS)[number]['provider'];
export type PointType = 'real' | 'provider';

export type GeoPoint = {
  id: string;
  latitude: number;
  longitude: number;
  title: string;
  colour: string;
  pointType: PointType;
  provider?: ProviderName;
  errorMeters?: number;
  addressText?: string;
  realLat?: number;
  realLon?: number;
  geocoderLat?: number;
  geocoderLon?: number;
};

export type GeoJSONPointProperties = {
  id: string;
  title: string;
  colour: string;
  pointType: PointType;
  provider: ProviderName | null;
  errorMeters: number | null;
  addressText: string | null;
  realLat: number | null;
  realLon: number | null;
  geocoderLat: number | null;
  geocoderLon: number | null;
};

export type GeoJSONLineProperties = {
  provider: string;
  colour: string;
};

export type GeoJSONPointFeature = Feature<Point, GeoJSONPointProperties>;
export type GeoJSONPointFeatureCollection = FeatureCollection<Point, GeoJSONPointProperties>;

export type GeoJSONLineFeature = Feature<LineString, GeoJSONLineProperties>;
export type GeoJSONLineFeatureCollection = FeatureCollection<LineString, GeoJSONLineProperties>;
