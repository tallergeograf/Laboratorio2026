export class GeocoderError extends Error {
  constructor(message: string) {
    super(message)
    this.name = 'GeocoderError'
  }
}
