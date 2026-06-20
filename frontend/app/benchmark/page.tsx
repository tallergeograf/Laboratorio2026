import Link from 'next/link'
import { ChartBar, Map } from 'lucide-react'
import { ProcessCard } from './ProcessCard'

export default function BenchmarkPage() {
  return (
    <div className="p-8">
      <p className="text-sm text-white/40 mb-6">Seleccioná una sección para comenzar</p>
      <div className="grid grid-cols-3 gap-3 max-w-2xl">

        <Link
          href="/benchmark/analisis"
          className="group flex flex-col gap-3 p-5 rounded-xl border border-white/10 hover:border-white/25 transition-colors bg-white/5"
        >
          <div className="w-10 h-10 rounded-lg bg-blue-500/10 flex items-center justify-center">
            <ChartBar className="w-5 h-5 text-blue-400" />
          </div>
          <div>
            <p className="text-sm font-medium text-white mb-1">Análisis</p>
            <p className="text-xs text-white/40 leading-relaxed">Comparativa de métricas entre geocoders: precisión, cobertura, latencia y confiabilidad.</p>
          </div>
          <span className="text-xs text-blue-400 flex items-center gap-1 mt-auto">
            Ver análisis →
          </span>
        </Link>

        <Link
          href="/benchmark/map"
          className="group flex flex-col gap-3 p-5 rounded-xl border border-white/10 hover:border-white/25 transition-colors bg-white/5"
        >
          <div className="w-10 h-10 rounded-lg bg-emerald-500/10 flex items-center justify-center">
            <Map className="w-5 h-5 text-emerald-400" />
          </div>
          <div>
            <p className="text-sm font-medium text-white mb-1">Mapa</p>
            <p className="text-xs text-white/40 leading-relaxed">Visualización geoespacial de los resultados sobre el territorio uruguayo.</p>
          </div>
          <span className="text-xs text-emerald-400 flex items-center gap-1 mt-auto">
            Ver mapa →
          </span>
        </Link>

        <ProcessCard />

      </div>
    </div>
  )
}