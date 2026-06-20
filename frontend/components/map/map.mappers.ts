import type { GeoPoint, ProviderName } from './map.types';
import type { GeoAPIStatsResponse, GeoAPIAddress } from '@/lib/models/geo_api';
import { providerColourByName } from './map.constants';

type GeoAPIStatsResponseElement = GeoAPIStatsResponse[number];

export function statsToGeoPoints(stats: GeoAPIStatsResponse): GeoPoint[] {
  if (!Array.isArray(stats) || stats.length === 0) return [];

  const realPoints = stats[0].addresses.map((addr: GeoAPIAddress) => ({
    id: `real-${addr.addressId}`,
    latitude: addr.realLat,
    longitude: addr.realLon,
    title: `Posición real · ${addr.addressText}`,
    colour: '#33FFF6',
    pointType: 'real' as const,
    addressText: addr.addressText,
  }));

  const providerPoints = stats.flatMap((providerStats: GeoAPIStatsResponseElement) => {
    const colour = providerColourByName[providerStats.provider] ?? '#888888';
    const provider = providerStats.provider as ProviderName;

    return providerStats.addresses.map((addr: GeoAPIAddress) => ({
      id: `provider-${provider}-${addr.addressId}`,
      latitude: addr.geocoderLat,
      longitude: addr.geocoderLon,
      title: `${provider} · ${addr.addressText}`,
      colour,
      pointType: 'provider' as const,
      provider,
      errorMeters: addr.errorMeters,
      addressText: addr.addressText,
      realLat: addr.realLat,
      realLon: addr.realLon,
      geocoderLat: addr.geocoderLat,
      geocoderLon: addr.geocoderLon,
    }));
  });

  return [...realPoints, ...providerPoints];
}

