import { z } from 'zod'

// -- Address --

export const NominatimAddressSchema = z.object({
  house_number: z.string().optional(),
  road: z.string().optional(),
  suburb: z.string().optional(),
  neighbourhood: z.string().optional(),
  quarter: z.string().optional(),
  city: z.string().optional(),
  town: z.string().optional(),
  village: z.string().optional(),
  municipality: z.string().optional(),
  county: z.string().optional(),
  state: z.string().optional(),
  'ISO3166-2-lvl4': z.string().optional(),
  postcode: z.string().optional(),
  country: z.string().optional(),
  country_code: z.string().optional(),
})

export type NominatimAddress = z.infer<typeof NominatimAddressSchema>

// -- Place (shared shape for search / reverse / lookup) --

export const NominatimPlaceSchema = z.object({
  place_id: z.number(),
  licence: z.string().optional(),
  osm_type: z.enum(['node', 'way', 'relation']),
  osm_id: z.number(),
  lat: z.string(),
  lon: z.string(),
  place_rank: z.number().optional(),
  category: z.string().optional(),
  type: z.string().optional(),
  importance: z.number().optional(),
  addresstype: z.string().optional(),
  name: z.string().optional(),
  display_name: z.string(),
  address: NominatimAddressSchema.optional(),
  boundingbox: z.tuple([z.string(), z.string(), z.string(), z.string()]).optional(),
})

export type NominatimPlace = z.infer<typeof NominatimPlaceSchema>

// -- /search --

export const NominatimSearchParamsSchema = z
  .object({
    q: z.string().optional(),
    street: z.string().optional(),
    city: z.string().optional(),
    county: z.string().optional(),
    state: z.string().optional(),
    country: z.string().optional(),
    postalcode: z.string().optional(),
    format: z.enum(['json', 'jsonv2', 'geojson', 'geocodejson']).default('jsonv2'),
    limit: z.number().int().min(1).max(40).optional(),
    countrycodes: z.string().optional(),
    addressdetails: z.literal(1).optional(),
    extratags: z.literal(1).optional(),
    namedetails: z.literal(1).optional(),
    viewbox: z.string().optional(),
    bounded: z.literal(1).optional(),
    'accept-language': z.string().optional(),
  })
  .refine(
    (d) => (d.q?.trim()) || (d.street?.trim()) || (d.city?.trim()) || (d.county?.trim()) || (d.state?.trim()) || (d.country?.trim()) || (d.postalcode?.trim()),
    { message: 'Provide q or at least one structured param (street, city, county, state, country, postalcode)' }
  )

export type NominatimSearchParams = z.input<typeof NominatimSearchParamsSchema>

export const NominatimSearchResultSchema = NominatimPlaceSchema
export const NominatimSearchResultsSchema = z.array(NominatimPlaceSchema)
export type NominatimSearchResult = z.infer<typeof NominatimSearchResultSchema>
export type NominatimSearchResults = z.infer<typeof NominatimSearchResultsSchema>

// -- /reverse --

export const NominatimReverseParamsSchema = z.object({
  lat: z.number().min(-90).max(90),
  lon: z.number().min(-180).max(180),
  format: z.enum(['json', 'jsonv2', 'geojson', 'geocodejson']).default('jsonv2'),
  zoom: z.number().int().min(0).max(18).optional(),
  addressdetails: z.literal(1).optional(),
  layer: z.string().optional(),
})

export type NominatimReverseParams = z.input<typeof NominatimReverseParamsSchema>

export const NominatimErrorCheckSchema = z.object({
  error: z.union([
    z.string(),
    z.object({ code: z.number(), message: z.string() }),
  ]),
})
export const NominatimReverseResultSchema = NominatimPlaceSchema
export type NominatimReverseResult = z.infer<typeof NominatimReverseResultSchema>


// -- /lookup --

export const NominatimLookupParamsSchema = z.object({
  osm_ids: z.string().min(1),
  format: z.enum(['json', 'jsonv2', 'geojson', 'geocodejson']).default('jsonv2'),
  addressdetails: z.literal(1).optional(),
})

export type NominatimLookupParams = z.input<typeof NominatimLookupParamsSchema>

export const NominatimLookupResultSchema = NominatimPlaceSchema
export const NominatimLookupResultsSchema = z.array(NominatimPlaceSchema)
export type NominatimLookupResult = z.infer<typeof NominatimLookupResultSchema>
export type NominatimLookupResults = z.infer<typeof NominatimLookupResultsSchema>

// -- /details --

export const NominatimDetailsParamsSchema = z
  .object({
    place_id: z.number().int().positive().optional(),
    osmtype: z.enum(['N', 'W', 'R']).optional(),
    osmid: z.number().int().positive().optional(),
    format: z.literal('json').default('json'),
  })
  .refine((d) => d.place_id !== undefined || (d.osmtype && d.osmid !== undefined), {
    message: 'Provide place_id or both osmtype and osmid',
  })

export type NominatimDetailsParams = z.input<typeof NominatimDetailsParamsSchema>

const NominatimGeoJsonPointSchema = z.object({
  type: z.literal('Point'),
  coordinates: z.tuple([z.number(), z.number()]),
})

export const NominatimDetailsResultSchema = z
  .object({
    place_id: z.number(),
    parent_place_id: z.number().optional(),
    osm_type: z.string().optional(),
    osm_id: z.number().optional(),
    category: z.string().optional(),
    type: z.string().optional(),
    admin_level: z.number().optional(),
    localname: z.string().optional(),
    names: z.record(z.string(), z.string()).optional(),
    addresstags: z.record(z.string(), z.string()).optional(),
    housenumber: z.string().optional(),
    calculated_postcode: z.string().optional(),
    country_code: z.string().optional(),
    indexed_date: z.string().optional(),
    importance: z.number().optional(),
    calculated_importance: z.number().optional(),
    extratags: z.record(z.string(), z.unknown()).optional(),
    rank_address: z.number().optional(),
    rank_search: z.number().optional(),
    isarea: z.boolean().optional(),
    centroid: NominatimGeoJsonPointSchema.optional(),
    geometry: NominatimGeoJsonPointSchema.optional(),
  })
  .catchall(z.unknown())

export type NominatimDetailsResult = z.infer<typeof NominatimDetailsResultSchema>

// -- /status --

export const NominatimStatusSchema = z.object({
  status: z.number(),
  message: z.string(),
  data_updated: z.string().optional(),
  software_version: z.string().optional(),
  database_version: z.string().optional(),
})

export type NominatimStatus = z.infer<typeof NominatimStatusSchema>
