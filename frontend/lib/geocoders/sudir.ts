import type {
  SudirGeocodeParams,
  SudirGeocodeResults,
  SudirReverseParams,
  SudirReverseResults,
} from '@/lib/models/sudir'
import {
  parseSudirGeocodeParams,
  parseSudirReverseParams,
  createSudirGeocodeResultsAdapter,
  createSudirReverseResultsAdapter,
} from '@/lib/adapters/sudir'
import { BASE_URL, ENDPOINTS, HEADERS } from '@/lib/constants/sudir'
import { UrlBuilder } from '@/lib/utils/url-builder'
import { GeocoderError } from '@/lib/utils/geocoder-error'

export async function sudirGeocode(
  params: SudirGeocodeParams
): Promise<SudirGeocodeResults> {
  const parsed = parseSudirGeocodeParams(params)
  const url = new UrlBuilder().base(BASE_URL).path(ENDPOINTS.geocode).params(parsed).build()
  const res = await fetch(url, { headers: HEADERS, signal: AbortSignal.timeout(10_000), cache: 'no-store' })
  if (!res.ok) {
    const msg = res.status === 429
      ? `Rate limited (429) — slow down requests`
      : `HTTP ${res.status}: ${res.statusText}`
    throw new GeocoderError(msg)
  }
  let data: unknown
  try { data = await res.json() } catch { throw new GeocoderError('SuDir BusquedaDireccion returned non-JSON response') }
  return createSudirGeocodeResultsAdapter(data)
}

export async function sudirReverse(
  params: SudirReverseParams
): Promise<SudirReverseResults> {
  const parsed = parseSudirReverseParams(params)
  const url = new UrlBuilder().base(BASE_URL).path(ENDPOINTS.reverse).params(parsed).build()
  const res = await fetch(url, { headers: HEADERS, signal: AbortSignal.timeout(10_000), cache: 'no-store' })
  if (!res.ok) {
    const msg = res.status === 429
      ? `Rate limited (429) — slow down requests`
      : `HTTP ${res.status}: ${res.statusText}`
    throw new GeocoderError(msg)
  }
  let data: unknown
  try { data = await res.json() } catch { throw new GeocoderError('SuDir /reverse returned non-JSON response') }
  return createSudirReverseResultsAdapter(data)
}
