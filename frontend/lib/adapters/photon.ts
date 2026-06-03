import {
  PhotonSearchParamsSchema,
  PhotonReverseParamsSchema,
  PhotonStructuredParamsSchema,
  PhotonPropertiesSchema,
  PhotonFeatureSchema,
  PhotonFeatureCollectionSchema,
  PhotonStatusSchema,
} from '@/lib/models/photon'
import type {
  PhotonSearchParams,
  PhotonReverseParams,
  PhotonStructuredParams,
  PhotonProperties,
  PhotonFeature,
  PhotonFeatureCollection,
  PhotonStatus,
} from '@/lib/models/photon'

export const parsePhotonSearchParams = (params: PhotonSearchParams) =>
  PhotonSearchParamsSchema.parse(params)

export const parsePhotonReverseParams = (params: PhotonReverseParams) =>
  PhotonReverseParamsSchema.parse(params)

export const parsePhotonStructuredParams = (params: PhotonStructuredParams) =>
  PhotonStructuredParamsSchema.parse(params)

export const createPhotonPropertiesAdapter = (data: unknown): PhotonProperties =>
  PhotonPropertiesSchema.parse(data)

export const createPhotonFeatureAdapter = (data: unknown): PhotonFeature =>
  PhotonFeatureSchema.parse(data)

// Used by /api, /reverse and /structured (all return FeatureCollection)
export const createPhotonFeatureCollectionAdapter = (data: unknown): PhotonFeatureCollection =>
  PhotonFeatureCollectionSchema.parse(data)

export const createPhotonStatusAdapter = (data: unknown): PhotonStatus =>
  PhotonStatusSchema.parse(data)
