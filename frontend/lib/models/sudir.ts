import { z } from 'zod'

// -- /api/v0/geocode/BusquedaDireccion --

export const SudirGeocodeParamsSchema = z.object({
  calle: z.string().min(1),
  departamento: z.string().min(1),
  localidad: z.string().min(1),
})

export type SudirGeocodeParams = z.input<typeof SudirGeocodeParamsSchema>

const SudirInmuebleSchema = z.object({
  nombre: z.string(),
  idPuntoNotable: z.number(),
})

const SudirDireccionSchema = z.object({
  departamento: z.object({
    idDepartamento: z.number(),
    nombre_normalizado: z.string(),
  }),
  localidad: z.object({
    idLocalidad: z.number(),
    nombre_normalizado: z.string(),
  }),
  calle: z.object({
    idCalle: z.number(),
    nombre_normalizado: z.string(),
  }).optional(),
  numero: z.object({
    nro_puerta: z.number(),
  }).optional(),
  inmueble: SudirInmuebleSchema.optional(),
})

export const SudirGeocodeResultSchema = z.object({
  direccion: SudirDireccionSchema,
  codigoPostal: z.number(),
  codigoPostalAmpliado: z.number(),
  puntoX: z.number(),
  puntoY: z.number(),
  idPunto: z.number(),
  srid: z.number(),
  idTipoClasificacion: z.number(),
  error: z.string(),
})

export const SudirGeocodeResultsSchema = z.array(SudirGeocodeResultSchema)
export type SudirGeocodeResult = z.infer<typeof SudirGeocodeResultSchema>
export type SudirGeocodeResults = z.infer<typeof SudirGeocodeResultsSchema>

// -- /api/v1/geocode/reverse --

export const SudirReverseParamsSchema = z.object({
  latitud: z.number().min(-90).max(90),
  longitud: z.number().min(-180).max(180),
  limit: z.number().int().min(1).optional(),
})

export type SudirReverseParams = z.input<typeof SudirReverseParamsSchema>

export const SudirReverseResultSchema = z.object({
  type: z.string(),
  id: z.string(),
  address: z.string(),
  idCalle: z.number(),
  nomVia: z.string(),
  postalCode: z.string(),
  idLocalidad: z.number(),
  localidad: z.string(),
  idDepartamento: z.number(),
  departamento: z.string(),
  manzana: z.string().nullable(),
  solar: z.string().nullable(),
  inmueble: z.string().nullable(),
  idCalleEsq: z.number(),
  km: z.number(),
  priority: z.number(),
  geom: z.unknown().nullable(),
  tip_via: z.string().nullable(),
  lat: z.number(),
  lng: z.number(),
  portalNumber: z.number(),
  letra: z.string().nullable(),
  stateMsg: z.string(),
  source: z.string(),
  ranking: z.number(),
  state: z.number(),
})

export const SudirReverseResultsSchema = z.array(SudirReverseResultSchema)
export type SudirReverseResult = z.infer<typeof SudirReverseResultSchema>
export type SudirReverseResults = z.infer<typeof SudirReverseResultsSchema>
