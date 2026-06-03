import type {
  PhotonSearchParams,
  PhotonFeatureCollection,
  PhotonReverseParams,
  PhotonStructuredParams,
  PhotonStatus,
} from '@/lib/models/photon'
import {
  parsePhotonSearchParams,
  parsePhotonReverseParams,
  parsePhotonStructuredParams,
  createPhotonFeatureCollectionAdapter,
  createPhotonStatusAdapter,
} from '@/lib/adapters/photon'
import { BASE_URL, ENDPOINTS, HEADERS } from '@/lib/constants/photon'
import { UrlBuilder } from '@/lib/utils/url-builder'
import { GeocoderError } from '@/lib/utils/geocoder-error'

export async function photonSearch(
  params: PhotonSearchParams
): Promise<PhotonFeatureCollection> {
  const parsed = parsePhotonSearchParams(params)
  const url = new UrlBuilder().base(BASE_URL).path(ENDPOINTS.api).params(parsed).build()
  const res = await fetch(url, { headers: HEADERS, signal: AbortSignal.timeout(10_000), cache: 'no-store' })
  if (!res.ok) {
    const msg = res.status === 429
      ? `Rate limited (429) — slow down requests`
      : `HTTP ${res.status}: ${res.statusText}`
    throw new GeocoderError(msg)
  }
  let data: unknown
  try { data = await res.json() } catch { throw new GeocoderError('Photon /api returned non-JSON response') }
  return createPhotonFeatureCollectionAdapter(data)
}

export async function photonReverse(
  params: PhotonReverseParams
): Promise<PhotonFeatureCollection> {
  const parsed = parsePhotonReverseParams(params)
  const url = new UrlBuilder().base(BASE_URL).path(ENDPOINTS.reverse).params(parsed).build()
  const res = await fetch(url, { headers: HEADERS, signal: AbortSignal.timeout(10_000), cache: 'no-store' })
  if (!res.ok) {
    const msg = res.status === 429
      ? `Rate limited (429) — slow down requests`
      : `HTTP ${res.status}: ${res.statusText}`
    throw new GeocoderError(msg)
  }
  let data: unknown
  try { data = await res.json() } catch { throw new GeocoderError('Photon /reverse returned non-JSON response') }
  return createPhotonFeatureCollectionAdapter(data)
}

export async function photonStructured(
  params: PhotonStructuredParams
): Promise<PhotonFeatureCollection> {
  const parsed = parsePhotonStructuredParams(params)
  const url = new UrlBuilder().base(BASE_URL).path(ENDPOINTS.structured).params(parsed).build()
  const res = await fetch(url, { headers: HEADERS, signal: AbortSignal.timeout(10_000), cache: 'no-store' })
  if (!res.ok) {
    const msg = res.status === 429
      ? `Rate limited (429) — slow down requests`
      : `HTTP ${res.status}: ${res.statusText}`
    throw new GeocoderError(msg)
  }
  let data: unknown
  try { data = await res.json() } catch { throw new GeocoderError('Photon /structured returned non-JSON response') }
  return createPhotonFeatureCollectionAdapter(data)
}

export async function photonStatus(): Promise<PhotonStatus> {
  const url = new UrlBuilder().base(BASE_URL).path(ENDPOINTS.status).build()
  const res = await fetch(url, { headers: HEADERS, signal: AbortSignal.timeout(10_000), cache: 'no-store' })
  if (!res.ok) {
    const msg = res.status === 429
      ? `Rate limited (429) — slow down requests`
      : `HTTP ${res.status}: ${res.statusText}`
    throw new GeocoderError(msg)
  }
  let data: unknown
  try { data = await res.json() } catch { throw new GeocoderError('Photon /status returned non-JSON response') }
  return createPhotonStatusAdapter(data)
}
