export const BASE_URL = 'https://nominatim.openstreetmap.org'

export const ENDPOINTS = {
  search: '/search',
  reverse: '/reverse',
  lookup: '/lookup',
  details: '/details',
  status: '/status',
} as const

// Called at request time so NOMINATIM_EMAIL is read at runtime, not build time
export function getHeaders(): Record<string, string> {
  const headers: Record<string, string> = {
    'User-Agent': 'benchmark-geocoders/1.0',
    ...(process.env.APP_URL ? { 'Referer': process.env.APP_URL } : {}),
  }
  if (process.env.NOMINATIM_EMAIL) {
    headers['Email'] = process.env.NOMINATIM_EMAIL
  }
  return headers
}
