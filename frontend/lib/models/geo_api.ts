import {z} from 'zod'

export const GeoAPIAddressSchema = z.object({
  addressId: z.number(),
  addressText: z.string(),
  realLat: z.number(),
  realLon: z.number(),
  geocoderLat: z.number(),
  geocoderLon: z.number(),
  errorMeters: z.number(),
})
export type GeoAPIAddress = z.infer<typeof GeoAPIAddressSchema>

export const GeoAPIAccuracyStatsSchema = z.object({
  averageError: z.number(),
  maxError: z.number(),
  medianError: z.number(),
  percentageWithinMeters: z.number(),
})

export const GeoAPICoverageStatsSchema = z.object({
  coverage: z.number(),
})

export const GeoAPIReliabilityStatsSchema = z.object({
  totalErrorsTypographic: z.number(),
  totalErrorsPermutation: z.number(),
  totalErrorsRural: z.number(),
  totalErrorsUrban: z.number(),
  totalErrorsAbbreviation: z.number(),
  totalErrorsComun: z.number(),
})

export const GeoAPILatencyStatsSchema = z.object({
  averageLatencyMs: z.number(),
  medianLatencyMs: z.number(),
  maxLatencyMs: z.number(),
})

export const GeoAPIStatsResponseElementSchema = z.object({
  provider: z.string(),
  sampleSize: z.number(),
  accuracyStats: GeoAPIAccuracyStatsSchema,
  coverageStats: GeoAPICoverageStatsSchema,
  reliabilityStats: GeoAPIReliabilityStatsSchema,
  latencyStats: GeoAPILatencyStatsSchema,
  addresses: z.array(GeoAPIAddressSchema),
})
export type GeoAPIStatsResponseElement = z.infer<typeof GeoAPIStatsResponseElementSchema>

// -- Process --
export const GeoAPIProcessRequestSchema = z.object({})
export type GeoAPIProcessRequest = z.infer<typeof GeoAPIProcessRequestSchema>

// export const GeoAPIProcessResponseSchema = z.array(GeoAPIProcessResponseElementSchema)
// export type GeoAPIProcessResponse = z.infer<typeof GeoAPIProcessResponseSchema>

// -- Stats --

export const GeoAPIStatsFilterRequestSchema = z.object({
  departments: z.array(z.string()).optional(),
  category: z.array(z.string()).optional(),
  variacion: z.array(z.string()).optional(),
})
export type GeoAPIStatsFilterRequest = z.infer<typeof GeoAPIStatsFilterRequestSchema>

export const GeoAPIStatsRequestSchema = z.object({
  demo: z.boolean().optional(),
  filters: GeoAPIStatsFilterRequestSchema.optional(),
})
export type GeoAPIStatsRequest = z.infer<typeof GeoAPIStatsRequestSchema>

export const GeoAPIStatsResponseSchema = z.array(GeoAPIStatsResponseElementSchema)
export type GeoAPIStatsResponse = z.infer<typeof GeoAPIStatsResponseSchema>