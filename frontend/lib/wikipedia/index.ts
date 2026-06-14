import { BASE_URL, ENDPOINTS } from '@/lib/constants/wikipedia'
import {
  createWikipediaSearchResultAdapter,
  createWikipediaSummaryAdapter,
} from '@/lib/adapters/wikipedia'
import type { WikipediaSummary } from '@/lib/models/wikipedia'

export type { WikipediaSummary }

async function searchWikipediaTitle(name: string): Promise<string | null> {
  const url = new URL(BASE_URL + ENDPOINTS.search)
  url.searchParams.set('action', 'query')
  url.searchParams.set('list', 'search')
  url.searchParams.set('srsearch', name)
  url.searchParams.set('srlimit', '1')
  url.searchParams.set('format', 'json')
  url.searchParams.set('origin', '*')

  const res = await fetch(url.toString())
  if (!res.ok) return null
  const data = createWikipediaSearchResultAdapter(await res.json())
  return data.query?.search?.[0]?.title ?? null
}

export async function fetchWikipediaSummary(name: string): Promise<WikipediaSummary | null> {
  try {
    const title = await searchWikipediaTitle(name)
    if (!title) return null

    const res = await fetch(`${BASE_URL}${ENDPOINTS.summary}/${encodeURIComponent(title)}`)
    if (!res.ok) return null
    return createWikipediaSummaryAdapter(await res.json())
  } catch {
    return null
  }
}
