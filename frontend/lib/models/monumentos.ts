import { z } from 'zod'

export const MonumentoSchema = z.object({
  id: z.number(),
  gid: z.number().nullable().optional(),
  nombre: z.string(),
  latitud: z.number(),
  longitud: z.number(),
  direccion: z.string().nullable().optional(),
  localidad: z.string().nullable().optional(),
})

export const MonumentosSchema = z.array(MonumentoSchema)

export type Monumento = z.infer<typeof MonumentoSchema>
export type Monumentos = z.infer<typeof MonumentosSchema>

export const NearestMonumentoSchema = z.object({
  id: z.number(),
  osmId: z.number().nullable().optional(),
  name: z.string().nullable().optional(),
  lat: z.number(),
  lon: z.number(),
  street: z.string().nullable().optional(),
  city: z.string().nullable().optional(),
  distanceMeters: z.number(),
  queryLat: z.number(),
  queryLon: z.number(),
})

export const NearestMonumentosSchema = z.array(NearestMonumentoSchema)

export type NearestMonumento = z.infer<typeof NearestMonumentoSchema>
export type NearestMonumentos = z.infer<typeof NearestMonumentosSchema>

export const NearestMonumentosParamsSchema = z.object({
  address: z.string().min(1, 'Address is required'),
  provider: z.enum(['NOMINATIM', 'PHOTON', 'SUDIR']),
  limit: z.number().int().min(1).default(20),
})

export type NearestMonumentosParams = z.input<typeof NearestMonumentosParamsSchema>

export const WithinRadiusMonumentosParamsSchema = z.object({
  address: z.string().min(1, 'Address is required'),
  provider: z.enum(['NOMINATIM', 'PHOTON', 'SUDIR']),
  radius: z.number().min(1).max(5000).default(500),
})

export type WithinRadiusMonumentosParams = z.input<typeof WithinRadiusMonumentosParamsSchema>
