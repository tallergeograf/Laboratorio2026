export class UrlBuilder {
  private _base = ''
  private _path = ''
  private _entries: Array<[string, string]> = []

  base(url: string): this {
    this._base = url
    return this
  }

  path(segment: string): this {
    this._path = segment
    return this
  }

  params(record: Record<string, unknown>): this {
    for (const [k, v] of Object.entries(record)) {
      if (v === undefined || v === null) continue
      if (typeof v === 'object' && !Array.isArray(v)) {
        throw new Error(`UrlBuilder: unsupported param type for key "${k}": object`)
      }
      if (Array.isArray(v)) {
        for (const item of v) this._entries.push([k, String(item)])
      } else {
        this._entries.push([k, String(v)])
      }
    }
    return this
  }

  build(): string {
    if (!this._base) throw new Error('UrlBuilder: base URL not set before build()')
    const url = new URL(this._base + (this._path || ''))
    for (const [k, v] of this._entries) url.searchParams.append(k, v)
    return url.toString()
  }
}
