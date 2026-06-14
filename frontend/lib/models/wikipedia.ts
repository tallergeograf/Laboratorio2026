import { z } from 'zod'

export const WikipediaThumbnailSchema = z.object({
  source: z.string(),
  width: z.number(),
  height: z.number(),
})

export type WikipediaThumbnail = z.infer<typeof WikipediaThumbnailSchema>

export const WikipediaSummarySchema = z.object({
  title: z.string(),
  extract: z.string().optional(),
  thumbnail: WikipediaThumbnailSchema.optional(),
})

export type WikipediaSummary = z.infer<typeof WikipediaSummarySchema>

export const WikipediaSearchHitSchema = z.object({
  title: z.string(),
})

export const WikipediaSearchResultSchema = z.object({
  query: z
    .object({
      search: z.array(WikipediaSearchHitSchema).optional(),
    })
    .optional(),
})

export type WikipediaSearchResult = z.infer<typeof WikipediaSearchResultSchema>
