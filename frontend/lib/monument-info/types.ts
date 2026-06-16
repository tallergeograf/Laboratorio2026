export type MonumentInfo = {
  title: string
  extract?: string
  image?: { url: string; alt: string }
}

export interface MonumentInfoProvider {
  getInfo(name: string): Promise<MonumentInfo | null>
}
