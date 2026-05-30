'use client'

import { useState } from 'react'
import {
  nominatimSearch,
  nominatimReverse,
  nominatimLookup,
  nominatimDetails,
  nominatimStatus,
  photonSearch,
  photonReverse,
  photonStructured,
  photonStatus,
  sudirGeocode,
  sudirReverse,
} from '@/app/actions/geocode'

type FieldDef = {
  name: string
  label: string
  type: 'text' | 'number'
  required: boolean
}

type OperationDef = {
  label: string
  fields: FieldDef[]
  action: (inputs: Record<string, string>) => Promise<unknown>
}

const GEOCODER_CONFIG: Record<string, Record<string, OperationDef>> = {
  nominatim: {
    search: {
      label: 'Search',
      fields: [{ name: 'q', label: 'Query', type: 'text', required: true }],
      action: (i) => nominatimSearch({ q: i.q }),
    },
    reverse: {
      label: 'Reverse',
      fields: [
        { name: 'lat', label: 'Latitude', type: 'number', required: true },
        { name: 'lon', label: 'Longitude', type: 'number', required: true },
      ],
      action: (i) => nominatimReverse({ lat: Number(i.lat || 'NaN'), lon: Number(i.lon || 'NaN') }),
    },
    lookup: {
      label: 'Lookup',
      fields: [{ name: 'osm_ids', label: 'OSM IDs (e.g. N123,W456)', type: 'text', required: true }],
      action: (i) => nominatimLookup({ osm_ids: i.osm_ids }),
    },
    details: {
      label: 'Details',
      fields: [{ name: 'place_id', label: 'Place ID', type: 'number', required: true }],
      action: (i) => nominatimDetails({ place_id: i.place_id !== '' ? Number(i.place_id) : undefined }),
    },
    status: {
      label: 'Status',
      fields: [],
      action: () => nominatimStatus(),
    },
  },
  photon: {
    search: {
      label: 'Search',
      fields: [{ name: 'q', label: 'Query', type: 'text', required: true }],
      action: (i) => photonSearch({ q: i.q }),
    },
    reverse: {
      label: 'Reverse',
      fields: [
        { name: 'lat', label: 'Latitude', type: 'number', required: true },
        { name: 'lon', label: 'Longitude', type: 'number', required: true },
      ],
      action: (i) => photonReverse({ lat: Number(i.lat || 'NaN'), lon: Number(i.lon || 'NaN') }),
    },
    structured: {
      label: 'Structured',
      fields: [
        { name: 'city', label: 'City', type: 'text', required: false },
        { name: 'street', label: 'Street', type: 'text', required: false },
        { name: 'postcode', label: 'Postcode', type: 'text', required: false },
        { name: 'countrycode', label: 'Country code', type: 'text', required: false },
      ],
      action: (i) =>
        photonStructured({
          city: i.city || undefined,
          street: i.street || undefined,
          postcode: i.postcode || undefined,
          countrycode: i.countrycode || undefined,
        }),
    },
    status: {
      label: 'Status',
      fields: [],
      action: () => photonStatus(),
    },
  },
  sudir: {
    geocode: {
      label: 'Geocode',
      fields: [
        { name: 'calle', label: 'Calle', type: 'text', required: true },
        { name: 'departamento', label: 'Departamento', type: 'text', required: true },
        { name: 'localidad', label: 'Localidad', type: 'text', required: true },
      ],
      action: (i) =>
        sudirGeocode({ calle: i.calle, departamento: i.departamento, localidad: i.localidad }),
    },
    reverse: {
      label: 'Reverse',
      fields: [
        { name: 'latitud', label: 'Latitud', type: 'number', required: true },
        { name: 'longitud', label: 'Longitud', type: 'number', required: true },
      ],
      action: (i) => sudirReverse({ latitud: Number(i.latitud || 'NaN'), longitud: Number(i.longitud || 'NaN') }),
    },
  },
}

export default function Home() {
  const geocoderKeys = Object.keys(GEOCODER_CONFIG)

  const [geocoder, setGeocoder] = useState(geocoderKeys[0])
  const [operation, setOperation] = useState(Object.keys(GEOCODER_CONFIG[geocoderKeys[0]])[0])
  const [inputs, setInputs] = useState<Record<string, string>>({})
  const [result, setResult] = useState<unknown>(null)
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  const operationDef = GEOCODER_CONFIG[geocoder]?.[operation]

  function handleGeocoderChange(next: string) {
    const firstOp = Object.keys(GEOCODER_CONFIG[next])[0]
    setGeocoder(next)
    setOperation(firstOp)
    setInputs({})
    setResult(null)
    setError(null)
  }

  function handleOperationChange(next: string) {
    setOperation(next)
    setInputs({})
    setResult(null)
    setError(null)
  }

  function handleInputChange(name: string, value: string) {
    setInputs((prev) => ({ ...prev, [name]: value }))
  }

  async function handleSubmit(e: { preventDefault(): void }) {
    e.preventDefault()
    if (!operationDef) return
    setLoading(true)
    setResult(null)
    setError(null)
    try {
      const data = await operationDef.action(inputs)
      setResult(data)
    } catch (err) {
      setError(err instanceof Error ? err.message : String(err))
    } finally {
      setLoading(false)
    }
  }

  return (
    <main className="min-h-screen bg-zinc-50 dark:bg-zinc-950 p-8 font-sans">
      <div className="max-w-2xl mx-auto space-y-6">
        <h1 className="text-2xl font-semibold text-zinc-900 dark:text-zinc-50">Geocoder Playground</h1>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="flex gap-3">
            <div className="flex-1">
              <label htmlFor="geocoder-select" className="block text-sm font-medium text-zinc-700 dark:text-zinc-300 mb-1">
                Geocoder
              </label>
              <select
                id="geocoder-select"
                value={geocoder}
                onChange={(e) => handleGeocoderChange(e.target.value)}
                className="w-full rounded-md border border-zinc-300 dark:border-zinc-700 bg-white dark:bg-zinc-900 px-3 py-2 text-sm text-zinc-900 dark:text-zinc-100"
              >
                {geocoderKeys.map((key) => (
                  <option key={key} value={key}>
                    {key.charAt(0).toUpperCase() + key.slice(1)}
                  </option>
                ))}
              </select>
            </div>

            <div className="flex-1">
              <label htmlFor="operation-select" className="block text-sm font-medium text-zinc-700 dark:text-zinc-300 mb-1">
                Operation
              </label>
              <select
                id="operation-select"
                value={operation}
                onChange={(e) => handleOperationChange(e.target.value)}
                className="w-full rounded-md border border-zinc-300 dark:border-zinc-700 bg-white dark:bg-zinc-900 px-3 py-2 text-sm text-zinc-900 dark:text-zinc-100"
              >
                {Object.entries(GEOCODER_CONFIG[geocoder]).map(([key, op]) => (
                  <option key={key} value={key}>
                    {op.label}
                  </option>
                ))}
              </select>
            </div>
          </div>

          {operationDef && (
            <>
              {operationDef.fields.length > 0 ? (
                <div className="space-y-3">
                  {operationDef.fields.map((field) => (
                    <div key={field.name}>
                      <label htmlFor={`${geocoder}-${operation}-${field.name}`} className="block text-sm font-medium text-zinc-700 dark:text-zinc-300 mb-1">
                        {field.label}
                        {field.required && <span className="text-red-500 ml-1">*</span>}
                      </label>
                      <input
                        id={`${geocoder}-${operation}-${field.name}`}
                        type={field.type}
                        value={inputs[field.name] ?? ''}
                        onChange={(e) => handleInputChange(field.name, e.target.value)}
                        required={field.required}
                        className="w-full rounded-md border border-zinc-300 dark:border-zinc-700 bg-white dark:bg-zinc-900 px-3 py-2 text-sm text-zinc-900 dark:text-zinc-100"
                      />
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-sm text-zinc-500 dark:text-zinc-400">No inputs required.</p>
              )}

              <button
                type="submit"
                disabled={loading}
                className="w-full rounded-md bg-zinc-900 dark:bg-zinc-100 px-4 py-2 text-sm font-medium text-white dark:text-zinc-900 hover:bg-zinc-700 dark:hover:bg-zinc-300 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
              >
                {loading ? 'Loading…' : 'Submit'}
              </button>
            </>
          )}
        </form>

        {error && (
          <div role="alert" aria-live="assertive" className="rounded-md bg-red-50 dark:bg-red-950 border border-red-200 dark:border-red-800 p-4">
            <p className="text-sm font-medium text-red-800 dark:text-red-200">Error</p>
            <p className="text-sm text-red-700 dark:text-red-300 mt-1">{error}</p>
          </div>
        )}

        {result !== null && (
          <div role="region" aria-label="Response" aria-live="polite" className="rounded-md border border-zinc-200 dark:border-zinc-800 bg-white dark:bg-zinc-900 overflow-hidden">
            <div className="px-4 py-2 border-b border-zinc-200 dark:border-zinc-800">
              <span className="text-xs font-medium text-zinc-500 dark:text-zinc-400 uppercase tracking-wide">
                Response
              </span>
            </div>
            <pre tabIndex={0} aria-label="Response JSON" className="p-4 text-xs text-zinc-800 dark:text-zinc-200 overflow-auto max-h-96 font-mono">
              {(() => {
                try {
                  return JSON.stringify(result, null, 2)
                } catch {
                  return '[unserializable]'
                }
              })()}
            </pre>
          </div>
        )}
      </div>
    </main>
  )
}
