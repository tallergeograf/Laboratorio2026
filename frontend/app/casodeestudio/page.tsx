import { getMonumentos } from '@/lib/backend-api/monumentos'
import CasoDeEstudioClient from './components/CasoDeEstudioClient'

export default async function CasoDeEstudioPage() {
  const monumentos = await getMonumentos().catch(() => [])

  return (
    <div className="flex flex-col gap-4 p-6 w-full">
      <div>
        <h1 className="text-2xl font-semibold text-zinc-900 dark:text-zinc-50">
          Caso de Estudio — Monumentos de Montevideo
        </h1>
        <p className="text-sm text-zinc-500 dark:text-zinc-400 mt-1">
          Ingresá una dirección para encontrar los monumentos históricos más cercanos.
          {monumentos.length > 0 && ` (${monumentos.length} monumentos cargados)`}
        </p>
      </div>
      <CasoDeEstudioClient initialMonumentos={monumentos} />
    </div>
  )
}
