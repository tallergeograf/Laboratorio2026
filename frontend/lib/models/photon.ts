import { z } from 'zod'

// -- GeoJSON base --

export const PhotonPropertiesSchema = z.object({
  osm_id: z.number().optional(),
  osm_type: z.enum(['N', 'W', 'R']).optional(),
  osm_key: z.string().optional(),
  osm_value: z.string().optional(),
  name: z.string().optional(),
  housenumber: z.string().optional(),
  street: z.string().optional(),
  district: z.string().optional(),
  postcode: z.string().optional(),
  city: z.string().optional(),
  county: z.string().optional(),
  state: z.string().optional(),
  country: z.string().optional(),
  countrycode: z.string().optional(),
  type: z.string().optional(),
  extent: z.tuple([z.number(), z.number(), z.number(), z.number()]).optional(),
  extra: z.record(z.string(), z.unknown()).optional(),
})

export type PhotonProperties = z.infer<typeof PhotonPropertiesSchema>

export const PhotonFeatureSchema = z.object({
  type: z.literal('Feature'),
  geometry: z.object({
    type: z.enum(['Point', 'Polygon', 'LineString', 'MultiPolygon']),
    coordinates: z.array(z.unknown()),
  }),
  properties: PhotonPropertiesSchema,
})

export type PhotonFeature = z.infer<typeof PhotonFeatureSchema>

export const PhotonFeatureCollectionSchema = z.object({
  type: z.literal('FeatureCollection'),
  features: z.array(PhotonFeatureSchema),
})

export type PhotonFeatureCollection = z.infer<typeof PhotonFeatureCollectionSchema>

// -- /api --

const PhotonLangSchema = z.enum(['default', 'de', 'en', 'fr']).optional()

export const PhotonSearchParamsSchema = z.object({
  q: z.string().min(1),
  lat: z.number().min(-90).max(90).optional(),
  lon: z.number().min(-180).max(180).optional(),
  limit: z.number().int().min(1).optional(),
  lang: PhotonLangSchema,
  bbox: z.string().optional(),
  location_bias_scale: z.number().min(0).max(1).optional(),
  osm_tag: z.union([z.string(), z.array(z.string())]).optional(),
  layer: z.union([z.string(), z.array(z.string())]).optional(),
  dedupe: z.literal(0).optional(),
  debug: z.literal(1).optional(),
})

export type PhotonSearchParams = z.input<typeof PhotonSearchParamsSchema>

// -- /reverse --

export const PhotonReverseParamsSchema = z.object({
  lat: z.number().min(-90).max(90),
  lon: z.number().min(-180).max(180),
  radius: z.number().min(0).max(5000).optional(),
  limit: z.number().int().min(1).optional(),
  osm_tag: z.union([z.string(), z.array(z.string())]).optional(),
  lang: PhotonLangSchema,
})

export type PhotonReverseParams = z.input<typeof PhotonReverseParamsSchema>

// -- /structured --

export const PhotonStructuredParamsSchema = z.object({
  city: z.string().optional(),
  street: z.string().optional(),
  housenumber: z.string().optional(),
  postcode: z.string().optional(),
  district: z.string().optional(),
  county: z.string().optional(),
  state: z.string().optional(),
  countrycode: z.string().optional(),
  limit: z.number().int().min(1).optional(),
  lang: PhotonLangSchema,
  lat: z.number().min(-90).max(90).optional(),
  lon: z.number().min(-180).max(180).optional(),
  bbox: z.string().optional(),
  zoom: z.number().int().min(0).max(18).optional(),
  osm_tag: z.union([z.string(), z.array(z.string())]).optional(),
  layer: z.union([z.string(), z.array(z.string())]).optional(),
  location_bias_scale: z.number().min(0).max(1).optional(),
}).refine(
  (d) => (d.city?.trim()) || (d.street?.trim()) || (d.housenumber?.trim()) || (d.postcode?.trim()) || (d.countrycode?.trim()) || (d.state?.trim()) || (d.district?.trim()) || (d.county?.trim()),
  { message: 'Provide at least one structured field (city, street, housenumber, postcode, countrycode, state, district, county)' }
)

export type PhotonStructuredParams = z.input<typeof PhotonStructuredParamsSchema>

// -- /status --

export const PhotonStatusSchema = z.object({
  status: z.string(),
  import_date: z.string().optional(),
  version: z.string().optional(),
  git_commit: z.string().optional(),
})

export type PhotonStatus = z.infer<typeof PhotonStatusSchema>
