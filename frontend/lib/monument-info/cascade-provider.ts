import type { MonumentInfo, MonumentInfoProvider } from './types'

export class CascadeProvider implements MonumentInfoProvider {
  constructor(private readonly providers: MonumentInfoProvider[]) {}

  async getInfo(name: string): Promise<MonumentInfo | null> {
    for (const provider of this.providers) {
      const result = await provider.getInfo(name)
      if (result !== null) return result
    }
    return null
  }
}
