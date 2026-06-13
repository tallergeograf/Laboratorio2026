import {
  WikipediaSummarySchema,
  WikipediaSearchResultSchema,
} from '@/lib/models/wikipedia'
import type { WikipediaSummary, WikipediaSearchResult } from '@/lib/models/wikipedia'

export const createWikipediaSummaryAdapter = (data: unknown): WikipediaSummary =>
  WikipediaSummarySchema.parse(data)

export const createWikipediaSearchResultAdapter = (data: unknown): WikipediaSearchResult =>
  WikipediaSearchResultSchema.parse(data)
