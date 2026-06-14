import {
  MonumentosSchema,
  NearestMonumentosSchema,
  NearestMonumentosParamsSchema,
  WithinRadiusMonumentosParamsSchema,
} from '@/lib/models/monumentos'
import type { Monumentos, NearestMonumentos, NearestMonumentosParams, WithinRadiusMonumentosParams } from '@/lib/models/monumentos'

export function parseNearestMonumentosParams(params: NearestMonumentosParams): NearestMonumentosParams {
  return NearestMonumentosParamsSchema.parse(params)
}

export function parseWithinRadiusMonumentosParams(params: WithinRadiusMonumentosParams): WithinRadiusMonumentosParams {
  return WithinRadiusMonumentosParamsSchema.parse(params)
}

export function createMonumentosAdapter(data: unknown): Monumentos {
  return MonumentosSchema.parse(data)
}

export function createNearestMonumentosAdapter(data: unknown): NearestMonumentos {
  return NearestMonumentosSchema.parse(data)
}
