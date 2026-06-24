'use server'

import { ZodError } from 'zod'
import { GeocoderError } from '@/lib/utils/geocoder-error'
import {
  nominatimSearch as _nominatimSearch,
  nominatimReverse as _nominatimReverse,
  nominatimLookup as _nominatimLookup,
  nominatimDetails as _nominatimDetails,
  nominatimStatus as _nominatimStatus,
} from '@/lib/geocoders/nominatim'
import type {
  NominatimSearchParams,
  NominatimReverseParams,
  NominatimLookupParams,
  NominatimDetailsParams,
} from '@/lib/models/nominatim'

import {
  photonSearch as _photonSearch,
  photonReverse as _photonReverse,
  photonStructured as _photonStructured,
  photonStatus as _photonStatus,
} from '@/lib/geocoders/photon'
import type {
  PhotonSearchParams,
  PhotonReverseParams,
  PhotonStructuredParams,
} from '@/lib/models/photon'

import {
  sudirGeocode as _sudirGeocode,
  sudirReverse as _sudirReverse,
} from '@/lib/geocoders/sudir'
import type {
  SudirGeocodeParams,
  SudirReverseParams,
} from '@/lib/models/sudir'

// Wraps all geocoder calls: formats ZodErrors as readable strings, forwards
// typed GeocoderError instances, and sanitizes unknown internal errors to prevent
// leaking hostnames, TLS details, or other infra info to the client.
async function withGeocoderAction<T>(fn: () => Promise<T>): Promise<T> {
  try {
    return await fn()
  } catch (err) {
    if (err instanceof ZodError) {
      throw new Error(err.issues.map(i => `${i.path.join('.')}: ${i.message}`).join('; '))
    }
    if ((err instanceof Error || err instanceof DOMException) && (err as { name?: string }).name === 'TimeoutError') {
      throw new Error('Geocoder request timed out. Try again or check your connection.')
    }
    if (err instanceof GeocoderError) {
      throw new Error(err.message)
    }
    // Don't leak network/internal error details (DNS failures, TLS errors, etc.) to client
    throw new Error('Geocoder request failed. Check server logs for details.')
  }
}

export async function nominatimSearch(params: NominatimSearchParams) {
  return withGeocoderAction(() => _nominatimSearch(params))
}

export async function nominatimReverse(params: NominatimReverseParams) {
  return withGeocoderAction(() => _nominatimReverse(params))
}

export async function nominatimLookup(params: NominatimLookupParams) {
  return withGeocoderAction(() => _nominatimLookup(params))
}

export async function nominatimDetails(params: NominatimDetailsParams) {
  return withGeocoderAction(() => _nominatimDetails(params))
}

export async function nominatimStatus() {
  return withGeocoderAction(() => _nominatimStatus())
}

export async function photonSearch(params: PhotonSearchParams) {
  return withGeocoderAction(() => _photonSearch(params))
}

export async function photonReverse(params: PhotonReverseParams) {
  return withGeocoderAction(() => _photonReverse(params))
}

export async function photonStructured(params: PhotonStructuredParams) {
  return withGeocoderAction(() => _photonStructured(params))
}

export async function photonStatus() {
  return withGeocoderAction(() => _photonStatus())
}

export async function sudirGeocode(params: SudirGeocodeParams) {
  return withGeocoderAction(() => _sudirGeocode(params))
}

export async function sudirReverse(params: SudirReverseParams) {
  return withGeocoderAction(() => _sudirReverse(params))
}
