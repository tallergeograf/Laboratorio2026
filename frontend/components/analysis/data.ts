import { AccuracyMetric } from '@/app/constants/providers';

export const sampleAccuracyData: AccuracyMetric[] = [
  {
    metric: 'averageError',
    values: [
      { provider: 'Photon',    value: 5.2 },
      { provider: 'Sudir',     value: 3.8 },
      { provider: 'Nominatim', value: 7.5 },
    ]
  },
  {
    metric: 'medianError',
    values: [
      { provider: 'Photon',    value: 4.8 },
      { provider: 'Sudir',     value: 3.5 },
      { provider: 'Nominatim', value: 6.9 },
    ]
  },
  {
    metric: 'maxError',
    values: [
      { provider: 'Photon',    value: 12.3 },
      { provider: 'Sudir',     value: 8.2 },
      { provider: 'Nominatim', value: 15.7 },
    ]
  },
  {
    metric: "percentageWithinMeters",
    values: [
      { provider: 'Photon',    value: 78.5 },
      { provider: 'Sudir',     value: 85.2 },
      { provider: 'Nominatim', value: 65.4 },
    ]
  }
];