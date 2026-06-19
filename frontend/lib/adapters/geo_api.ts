import{
  BASE_URL,
  ENDPOINTS,
} from '../constants/geo_api'

import {
  GeoAPIProcessRequest,
  // GeoAPIProcessResponse,
  GeoAPIStatsRequest,
  GeoAPIStatsResponse,
} from '../models/geo_api'


export const GeoAPI = {
  async process(request: GeoAPIProcessRequest): Promise<string[]> {
    const params = new URLSearchParams({ demo: String(request.demo) })
    const res = await fetch(`${BASE_URL}${ENDPOINTS.process}?${params}`)
    if (!res.ok) {
      const msg = res.status === 429
        ? `Rate limited (429) — slow down requests`
        : `HTTP ${res.status}: ${res.statusText}`
      throw new Error(msg)
    }
    return res.json()
  },

  async stats(request?: GeoAPIStatsRequest): Promise<GeoAPIStatsResponse> {
    const res = await fetch(`${BASE_URL}${ENDPOINTS.stats}`, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(request ?? {}),
    })

    if (!res.ok) {
      const msg = res.status === 429
        ? `Rate limited (429) — slow down requests`
        : `HTTP ${res.status}: ${res.statusText}`
      throw new Error(msg)
    }

    const data: unknown = await (async () => { try { return await res.json() } catch { throw new Error('GeoAPI /stats returned non-JSON response') } })()
    return data as GeoAPIStatsResponse
  }
}