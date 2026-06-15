export type { MonumentInfo, MonumentInfoProvider } from './types'
export { CascadeProvider } from './cascade-provider'
export { WikipediaProvider } from './wikipedia-provider'

import { WikipediaProvider } from './wikipedia-provider'

export const defaultProvider = new WikipediaProvider()
