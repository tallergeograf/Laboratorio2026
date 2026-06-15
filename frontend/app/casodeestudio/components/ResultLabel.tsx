import type { SearchMode } from '@/app/casodeestudio/types'

type ResultLabelProps = {
  count: number
  searchMode: SearchMode
  radius: number
  searched: boolean
}

export default function ResultLabel({ count, searchMode, radius, searched }: ResultLabelProps) {
  if (count === 0) {
    if (!searched) return null
    return <p className="text-sm text-amber-600 dark:text-amber-400">No se encontraron monumentos para los datos ingresados.</p>
  }

  const label =
    searchMode === 'nearest'
      ? `${count} monumento${count !== 1 ? 's' : ''} más cercano${count !== 1 ? 's' : ''} encontrado${count !== 1 ? 's' : ''}`
      : `${count} monumento${count !== 1 ? 's' : ''} encontrado${count !== 1 ? 's' : ''} a menos de ${radius} m de tu dirección`

  return <p className="text-sm text-zinc-500">{label} — marcados en rojo</p>
}
