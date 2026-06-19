import { useEffect, useState } from 'react';
import { GeoAPI } from '@/lib/adapters/geo_api';
import { statsToGeoPoints } from '@/components/map/map.mappers';
import type { GeoPoint } from '@/components/map/map.types';
import type { GeoAPIStatsResponse } from '@/lib/models/geo_api';
import type { Filters } from '@/components/filters/types';

function filtersToRequest(filters: Filters) {
  return {
    demo: filters.demo.length > 0,
    filters: {
      departments: filters.departments.length ? filters.departments : undefined,
      category: filters.category.length ? filters.category : undefined,
      variacion: filters.variacion.length ? filters.variacion : undefined,
    },
  };
}

export function useGeoPoints(filters: Filters): {
  points: GeoPoint[];
  stats: GeoAPIStatsResponse;
  loading: boolean;
  error: Error | null;
} {
  const [points, setPoints] = useState<GeoPoint[]>([]);
  const [stats, setStats] = useState<GeoAPIStatsResponse>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);

  const { demo, departments, category, variacion } = filters;

  useEffect(() => {
    let mounted = true;
    setLoading(true);
    async function load() {
      try {
        const data = await GeoAPI.stats(filtersToRequest(filters));
        console.log('stats response:', JSON.stringify(data, null, 2));
        if (!mounted) return;
        setStats(data);
        setPoints(statsToGeoPoints(data));
      } catch (err: unknown) {
        if (!mounted) return;
        setError(err instanceof Error ? err : new Error(String(err)));
      } finally {
        if (mounted) setLoading(false);
      }
    }

    load();
    return () => {
      mounted = false;
    };
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [demo, departments, category, variacion]);

  return { points, stats, loading, error };
}
