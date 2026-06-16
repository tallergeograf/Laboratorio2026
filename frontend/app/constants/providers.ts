export const PROVIDER_COLORS = {
  Photon:    '#F97316',
  Sudir:     '#3B82F6',
  Nominatim: '#A78BFA',
} as const;

export type Provider = keyof typeof PROVIDER_COLORS;

export interface MetricValue {
  provider: Provider;
  value: number;
}

export interface AccuracyMetric {
  metric: string;
  values: MetricValue[];
}