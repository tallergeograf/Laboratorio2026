"use client"

import { useState } from "react"
import { RefreshCw, Loader2 } from "lucide-react"
import { GeoAPI } from "@/lib/adapters/geo_api"

export function ProcessCard() {
  const [loading, setLoading] = useState(false)
  const [result, setResult] = useState<{ ok: boolean; message: string } | null>(null)

  const handleProcess = async (demo: boolean) => {
    setLoading(true)
    setResult(null)
    try {
      const data = await GeoAPI.process({ demo })
      setResult({ ok: true, message: `Procesadas ${data.length} direcciones` })
    } catch (err) {
      setResult({ ok: false, message: err instanceof Error ? err.message : "Error desconocido" })
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="flex flex-col gap-3 p-5 rounded-xl border border-white/10 hover:border-white/25 transition-colors bg-white/5">
      <div className="w-10 h-10 rounded-lg bg-amber-500/10 flex items-center justify-center">
        {loading
          ? <Loader2 className="w-5 h-5 text-amber-400 animate-spin" />
          : <RefreshCw className="w-5 h-5 text-amber-400" />
        }
      </div>
      <div>
        <p className="text-sm font-medium text-white mb-1">Procesar datos</p>
        <p className="text-xs text-white/40 leading-relaxed">
          Ejecuta el geocoding sobre las direcciones y guarda los resultados.
        </p>
      </div>

      <div className="flex gap-2 mt-auto">
        <button
          disabled={loading}
          onClick={() => handleProcess(true)}
          className="flex-1 text-xs font-medium px-3 py-1.5 rounded-md bg-amber-500/15 text-amber-400 hover:bg-amber-500/25 transition-colors disabled:opacity-40 disabled:cursor-not-allowed"
        >
          Demo
        </button>
        <button
          disabled={loading}
          onClick={() => handleProcess(false)}
          className="flex-1 text-xs font-medium px-3 py-1.5 rounded-md bg-amber-500/15 text-amber-400 hover:bg-amber-500/25 transition-colors disabled:opacity-40 disabled:cursor-not-allowed"
        >
          Completo
        </button>
      </div>

      {result && (
        <p className={`text-xs ${result.ok ? "text-emerald-400" : "text-red-400"}`}>
          {result.message}
        </p>
      )}
    </div>
  )
}
