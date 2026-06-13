export const BACKEND_BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL ?? 'http://localhost:8081'

export const MONUMENTOS_ENDPOINTS = {
  all: '/api/monumentos',
  nearest: '/api/monumentos/nearest',
  within: '/api/monumentos/within',
} as const
