import type { SearchMode } from '@/app/casodeestudio/types'

type ResultLabelProps = {
  count: number
  searchMode: SearchMode
  radius: number
}

export default function ResultLabel({ count, searchMode, radius }: ResultLabelProps) {
  if (count === 0) return null

  const label =
    searchMode === 'nearest'
      ? `${count} monumento${count !== 1 ? 's' : ''} más cercano${count !== 1 ? 's' : ''} encontrado${count !== 1 ? 's' : ''}`
      : `${count} monumento${count !== 1 ? 's' : ''} encontrado${count !== 1 ? 's' : ''} a menos de ${radius} m de tu dirección`

  return <p className="text-sm text-zinc-500">{label} — marcados en rojo</p>
}
