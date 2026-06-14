export const PROVIDERS = [
  { value: 'NOMINATIM', label: 'Nominatim' },
  { value: 'PHOTON', label: 'Photon' },
  { value: 'SUDIR', label: 'Sudir (IMM)' },
] as const

export type Provider = (typeof PROVIDERS)[number]['value']
export type SearchMode = 'nearest' | 'radius'
