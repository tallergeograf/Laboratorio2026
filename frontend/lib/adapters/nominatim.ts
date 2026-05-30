import {
  NominatimSearchParamsSchema,
  NominatimReverseParamsSchema,
  NominatimLookupParamsSchema,
  NominatimDetailsParamsSchema,
  NominatimAddressSchema,
  NominatimPlaceSchema,
  NominatimReverseResultSchema,
  NominatimSearchResultsSchema,
  NominatimLookupResultsSchema,
  NominatimDetailsResultSchema,
  NominatimStatusSchema,
} from '@/lib/models/nominatim'
import type {
  NominatimSearchParams,
  NominatimReverseParams,
  NominatimLookupParams,
  NominatimDetailsParams,
  NominatimAddress,
  NominatimPlace,
  NominatimReverseResult,
  NominatimSearchResults,
  NominatimLookupResults,
  NominatimDetailsResult,
  NominatimStatus,
} from '@/lib/models/nominatim'

export const parseNominatimSearchParams = (params: NominatimSearchParams) =>
  NominatimSearchParamsSchema.parse(params)

export const parseNominatimReverseParams = (params: NominatimReverseParams) =>
  NominatimReverseParamsSchema.parse(params)

export const parseNominatimLookupParams = (params: NominatimLookupParams) =>
  NominatimLookupParamsSchema.parse(params)

export const parseNominatimDetailsParams = (params: NominatimDetailsParams) =>
  NominatimDetailsParamsSchema.parse(params)

export const createNominatimAddressAdapter = (data: unknown): NominatimAddress =>
  NominatimAddressSchema.parse(data)

export const createNominatimPlaceAdapter = (data: unknown): NominatimPlace =>
  NominatimPlaceSchema.parse(data)

export const createNominatimReverseResultAdapter = (data: unknown): NominatimReverseResult =>
  NominatimReverseResultSchema.parse(data)

// Used by /search and /lookup (both return arrays of places)
export const createNominatimSearchResultsAdapter = (data: unknown): NominatimSearchResults =>
  NominatimSearchResultsSchema.parse(data)

export const createNominatimLookupResultsAdapter = (data: unknown): NominatimLookupResults =>
  NominatimLookupResultsSchema.parse(data)

export const createNominatimDetailsResultAdapter = (data: unknown): NominatimDetailsResult =>
  NominatimDetailsResultSchema.parse(data)

export const createNominatimStatusAdapter = (data: unknown): NominatimStatus =>
  NominatimStatusSchema.parse(data)
