import type { GeoPoint, ProviderName } from './map.types';
import type { GeoAPIStatsResponse, GeoAPIAddress } from '@/lib/models/geo_api';
import { providerColourByName } from './map.constants';

type GeoAPIStatsResponseElement = GeoAPIStatsResponse[number];

export function statsToGeoPoints(stats: GeoAPIStatsResponse): GeoPoint[] {
  if (!Array.isArray(stats) || stats.length === 0) return [];

  const seenReal = new Set<number>();
  const realPoints = stats.flatMap((providerStats: GeoAPIStatsResponseElement) =>
    providerStats.realPoints
      .filter((rp) => !seenReal.has(rp.addressId) && seenReal.add(rp.addressId))
      .map((rp) => ({
        id: `real-${rp.addressId}`,
        latitude: rp.lat,
        longitude: rp.lon,
        title: `Posición real · ${rp.addressText}`,
        colour: '#33FFF6',
        pointType: 'real' as const,
        addressText: rp.addressText,
      }))
  );

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
