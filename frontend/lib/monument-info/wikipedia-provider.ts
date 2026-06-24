import { fetchWikipediaSummary } from '@/lib/wikipedia'
import type { MonumentInfo, MonumentInfoProvider } from './types'

export class WikipediaProvider implements MonumentInfoProvider {
  async getInfo(name: string): Promise<MonumentInfo | null> {
    const summary = await fetchWikipediaSummary(name)
    if (!summary) return null

    return {
      title: summary.title,
      extract: summary.extract,
      image: summary.thumbnail
        ? { url: summary.thumbnail.source, alt: summary.title }
        : undefined,
    }
  }
}
