import type {
  NominatimSearchParams,
  NominatimSearchResults,
  NominatimReverseParams,
  NominatimReverseResult,
  NominatimLookupParams,
  NominatimLookupResults,
  NominatimDetailsParams,
  NominatimDetailsResult,
  NominatimStatus,
} from '@/lib/models/nominatim'
import { NominatimErrorCheckSchema } from '@/lib/models/nominatim'
import {
  parseNominatimSearchParams,
  parseNominatimReverseParams,
  parseNominatimLookupParams,
  parseNominatimDetailsParams,
  createNominatimSearchResultsAdapter,
  createNominatimReverseResultAdapter,
  createNominatimLookupResultsAdapter,
  createNominatimDetailsResultAdapter,
  createNominatimStatusAdapter,
} from '@/lib/adapters/nominatim'
import { BASE_URL, ENDPOINTS, getHeaders } from '@/lib/constants/nominatim'
import { UrlBuilder } from '@/lib/utils/url-builder'
import { GeocoderError } from '@/lib/utils/geocoder-error'

// Nominatim can return {"error": "..."} or {"error": {"code": N, "message": "..."}} with HTTP 200
function checkNominatimError(data: unknown): void {
  const result = NominatimErrorCheckSchema.safeParse(data)
  if (result.success) {
    const e = result.data.error
    throw new GeocoderError(`Nominatim: ${typeof e === 'string' ? e : e.message}`)
  }
}

export async function nominatimSearch(
  params: NominatimSearchParams
): Promise<NominatimSearchResults> {
  const parsed = parseNominatimSearchParams(params)
  const url = new UrlBuilder().base(BASE_URL).path(ENDPOINTS.search).params(parsed).build()
  const res = await fetch(url, { headers: getHeaders(), signal: AbortSignal.timeout(10_000), cache: 'no-store' })
  if (!res.ok) {
    const msg = res.status === 429
      ? `Rate limited (429) — slow down requests`
      : `HTTP ${res.status}: ${res.statusText}`
    throw new GeocoderError(msg)
  }
  let data: unknown
  try { data = await res.json() } catch { throw new GeocoderError('Nominatim /search returned non-JSON response') }
  checkNominatimError(data)
  return createNominatimSearchResultsAdapter(data)
}

export async function nominatimReverse(
  params: NominatimReverseParams
): Promise<NominatimReverseResult> {
  const parsed = parseNominatimReverseParams(params)
  const url = new UrlBuilder().base(BASE_URL).path(ENDPOINTS.reverse).params(parsed).build()
  const res = await fetch(url, { headers: getHeaders(), signal: AbortSignal.timeout(10_000), cache: 'no-store' })
  if (!res.ok) {
    const msg = res.status === 429
      ? `Rate limited (429) — slow down requests`
      : `HTTP ${res.status}: ${res.statusText}`
    throw new GeocoderError(msg)
  }
  const data: unknown = await (async () => { try { return await res.json() } catch { throw new GeocoderError('Nominatim /reverse returned non-JSON response') } })()
  checkNominatimError(data)
  return createNominatimReverseResultAdapter(data)
}

export async function nominatimLookup(
  params: NominatimLookupParams
): Promise<NominatimLookupResults> {
  const parsed = parseNominatimLookupParams(params)
  const url = new UrlBuilder().base(BASE_URL).path(ENDPOINTS.lookup).params(parsed).build()
  const res = await fetch(url, { headers: getHeaders(), signal: AbortSignal.timeout(10_000), cache: 'no-store' })
  if (!res.ok) {
    const msg = res.status === 429
      ? `Rate limited (429) — slow down requests`
      : `HTTP ${res.status}: ${res.statusText}`
    throw new GeocoderError(msg)
  }
  let data: unknown
  try { data = await res.json() } catch { throw new GeocoderError('Nominatim /lookup returned non-JSON response') }
  checkNominatimError(data)
  return createNominatimLookupResultsAdapter(data)
}

export async function nominatimDetails(
  params: NominatimDetailsParams
): Promise<NominatimDetailsResult> {
  const parsed = parseNominatimDetailsParams(params)
  const url = new UrlBuilder().base(BASE_URL).path(ENDPOINTS.details).params(parsed).build()
  const res = await fetch(url, { headers: getHeaders(), signal: AbortSignal.timeout(10_000), cache: 'no-store' })
  if (!res.ok) {
    const msg = res.status === 429
      ? `Rate limited (429) — slow down requests`
      : `HTTP ${res.status}: ${res.statusText}`
    throw new GeocoderError(msg)
  }
  let data: unknown
  try { data = await res.json() } catch { throw new GeocoderError('Nominatim /details returned non-JSON response') }
  checkNominatimError(data)
  return createNominatimDetailsResultAdapter(data)
}

export async function nominatimStatus(): Promise<NominatimStatus> {
  const url = new UrlBuilder().base(BASE_URL).path(ENDPOINTS.status).params({ format: 'json' }).build()
  const res = await fetch(url, { headers: getHeaders(), signal: AbortSignal.timeout(10_000), cache: 'no-store' })
  if (!res.ok) {
    const msg = res.status === 429
      ? `Rate limited (429) — slow down requests`
      : `HTTP ${res.status}: ${res.statusText}`
    throw new GeocoderError(msg)
  }
  let data: unknown
  try { data = await res.json() } catch { throw new GeocoderError('Nominatim /status returned non-JSON response') }
  return createNominatimStatusAdapter(data)
}
