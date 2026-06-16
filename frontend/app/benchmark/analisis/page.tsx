'use client';

import { useMemo, useState } from 'react';
import { MetricChart } from '@/components/analysis/MetricChart';
import { useGeoPoints } from '@/hooks/useGeoPoints';
import { useFilters } from '@/app/benchmark/FiltersContext';
import type { GeoAPIStatsResponse } from '@/lib/models/geo_api';
import { Provider } from '@/app/constants/providers';

type Tab = 'accuracy' | 'coverage' | 'reliability';

function capitalize(s: string): string {
  return s.charAt(0).toUpperCase() + s.slice(1).toLowerCase();
}

function buildMetrics(
  stats: GeoAPIStatsResponse,
  items: Array<{
    key: string;
    label: string;
    unit: string;
    description: string;
    group: 'accuracyStats' | 'coverageStats' | 'reliabilityStats';
  }>
) {
  return items.map(({ key, label, unit, description, group }) => ({
    metric: label,
    unit,
    description,
    values: stats.map((s) => ({
      provider: capitalize(s.provider) as Provider,
      value: (s[group] as Record<string, number>)[key],
    })),
  }));
}

const ACCURACY_ITEMS = [
  { key: 'averageError',           label: 'Error Promedio',   unit: 'm',   description: 'Media del error sobre todas las mediciones',        group: 'accuracyStats' as const },
  { key: 'medianError',            label: 'Error Mediano',    unit: 'm',   description: 'Mediana del error de geocodificación',               group: 'accuracyStats' as const },
  { key: 'maxError',               label: 'Error Máximo',     unit: 'm',   description: 'Peor caso observado en la muestra',                  group: 'accuracyStats' as const },
  { key: 'percentageWithinMeters', label: '% Dentro de 13 m', unit: '%',  description: 'Proporción de resultados dentro del umbral de 13 m', group: 'accuracyStats' as const },
];

const COVERAGE_ITEMS = [
  { key: 'coverage', label: 'Cobertura', unit: '%', description: 'Porcentaje de direcciones geocodificadas exitosamente', group: 'coverageStats' as const },
];

const RELIABILITY_ITEMS = [
  { key: 'totalErrorsTypographic', label: 'Errores Tipográficos',   unit: 'err', description: 'Errores por variaciones tipográficas en la dirección',    group: 'reliabilityStats' as const },
  { key: 'totalErrorsPermutation', label: 'Errores de Permutación', unit: 'err', description: 'Errores por reordenamiento de palabras en la dirección',  group: 'reliabilityStats' as const },
  { key: 'totalErrorsRural',       label: 'Errores Rurales',        unit: 'err', description: 'Errores en direcciones de zonas rurales',                  group: 'reliabilityStats' as const },
  { key: 'totalErrorsUrban',       label: 'Errores Urbanos',        unit: 'err', description: 'Errores en direcciones de zonas urbanas',                  group: 'reliabilityStats' as const },
];

const TABS: Array<{ id: Tab; label: string }> = [
  { id: 'accuracy',    label: 'Precisión' },
  { id: 'coverage',   label: 'Cobertura' },
  { id: 'reliability', label: 'Confiabilidad' },
];

export default function Page() {
  const filters = useFilters();
  const { stats, loading, error } = useGeoPoints(filters);
  const [tab, setTab] = useState<Tab>('accuracy');

  const accuracyData    = useMemo(() => buildMetrics(stats, ACCURACY_ITEMS),    [stats]);
  const coverageData    = useMemo(() => buildMetrics(stats, COVERAGE_ITEMS),    [stats]);
  const reliabilityData = useMemo(() => buildMetrics(stats, RELIABILITY_ITEMS), [stats]);

  if (loading) {
    return (
      <main className="flex items-center justify-center min-h-screen bg-black text-white/40 text-sm">
        Cargando estadísticas…
      </main>
    );
  }

  if (error) {
    return (
      <main className="flex items-center justify-center min-h-screen bg-black text-red-400 text-sm">
        Error: {error.message}
      </main>
    );
  }

  const activeData =
    tab === 'accuracy'    ? accuracyData    :
    tab === 'coverage'    ? coverageData    :
                            reliabilityData;

  return (
    <main className="bg-black min-h-screen">
      <div className="flex gap-1 px-8 pt-8 border-b border-white/10">
        {TABS.map(({ id, label }) => (
          <button
            key={id}
            onClick={() => setTab(id)}
            className={[
              'px-4 py-2.5 text-sm font-medium transition-colors -mb-px',
              tab === id
                ? 'text-white border-b-2 border-white'
                : 'text-white/40 hover:text-white/70',
            ].join(' ')}
          >
            {label}
          </button>
        ))}
      </div>

      <div className={[
        'grid gap-5 p-8',
        tab === 'coverage' ? 'grid-cols-1 max-w-lg' : 'grid-cols-2',
      ].join(' ')}>
        {activeData.map(({ metric, unit, description, values }) => (
          <MetricChart
            key={metric}
            label={metric}
            description={description}
            unit={unit}
            values={values}
          />
        ))}
      </div>
    </main>
  );
}
