export const BASE_URL = 'https://photon.komoot.io'

export const ENDPOINTS = {
  api: '/api',
  reverse: '/reverse',
  structured: '/structured',
  status: '/status',
} as const

export const HEADERS = { 'User-Agent': 'benchmark-geocoders/1.0' } as const
