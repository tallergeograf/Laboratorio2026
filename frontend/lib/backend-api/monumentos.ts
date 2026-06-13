import type { Monumentos, NearestMonumentos, NearestMonumentosParams, WithinRadiusMonumentosParams } from '@/lib/models/monumentos'
import { parseNearestMonumentosParams, parseWithinRadiusMonumentosParams, createMonumentosAdapter, createNearestMonumentosAdapter } from '@/lib/adapters/monumentos'
import { BACKEND_BASE_URL, MONUMENTOS_ENDPOINTS } from '@/lib/constants/monumentos'

export async function getMonumentos(): Promise<Monumentos> {
  const url = `${BACKEND_BASE_URL}${MONUMENTOS_ENDPOINTS.all}`
  const res = await fetch(url, { cache: 'no-store' })
  if (!res.ok) throw new Error(`Failed to fetch monumentos: HTTP ${res.status}`)
  const data: unknown = await res.json()
  return createMonumentosAdapter(data)
}

export async function getNearestMonumentos(params: NearestMonumentosParams): Promise<NearestMonumentos> {
  const parsed = parseNearestMonumentosParams(params)
  const qs = new URLSearchParams({
    address: parsed.address,
    provider: parsed.provider,
    limit: String(parsed.limit),
  })
  const url = `${BACKEND_BASE_URL}${MONUMENTOS_ENDPOINTS.nearest}?${qs.toString()}`
  const res = await fetch(url, { cache: 'no-store' })
  if (!res.ok) throw new Error(`Failed to fetch nearest monumentos: HTTP ${res.status}`)
  const data: unknown = await res.json()
  return createNearestMonumentosAdapter(data)
}

export async function getMonumentosWithinRadius(params: WithinRadiusMonumentosParams): Promise<NearestMonumentos> {
  const parsed = parseWithinRadiusMonumentosParams(params)
  const qs = new URLSearchParams({
    address: parsed.address,
    provider: parsed.provider,
    radius: String(parsed.radius),
  })
  const url = `${BACKEND_BASE_URL}${MONUMENTOS_ENDPOINTS.within}?${qs.toString()}`
  const res = await fetch(url, { cache: 'no-store' })
  if (!res.ok) throw new Error(`Failed to fetch monumentos within radius: HTTP ${res.status}`)
  const data: unknown = await res.json()
  return createNearestMonumentosAdapter(data)
}
