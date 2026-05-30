export const BASE_URL = 'https://direcciones.ide.uy'

export const ENDPOINTS = {
  geocode: '/api/v0/geocode/BusquedaDireccion',
  reverse: '/api/v1/geocode/reverse',
} as const

export const HEADERS = { 'User-Agent': 'benchmark-geocoders/1.0' } as const
