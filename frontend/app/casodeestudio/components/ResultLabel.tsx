import type { SearchMode } from '@/app/casodeestudio/types'

type ResultLabelProps = {
  count: number
  searchMode: SearchMode
  radius: number
  searched: boolean
}

export default function ResultLabel({ count, searchMode, radius, searched }: ResultLabelProps) {
  if (!searched) return null

  if (count === 0) {
  return (
    <p className="text-sm text-amber-600 dark:text-amber-400">
      {searchMode === 'nearest'
        ? 'No se encontró la dirección ingresada.'
        : 'No se encontraron monumentos para los datos ingresados.'}
    </p>
  )
}
  const label =
    searchMode === 'nearest'
      ? `${count} monumento${count !== 1 ? 's' : ''} encontrado${count !== 1 ? 's' : ''}`
      : `${count} monumento${count !== 1 ? 's' : ''} a menos de ${radius} m`

  const dot = (color: string) => (
    <span style={{ width: 8, height: 8, borderRadius: '50%', background: color, display: 'inline-block', flexShrink: 0 }} />
  )

  return (
  <div className="flex items-center" style={{ width: '100%', position: 'relative' }}>
    <div
      className="flex items-center gap-2 px-3 py-1 rounded-md text-xs font-mono"
      style={{ border: '1px solid rgba(153,27,27,0.6)', background: 'rgba(69,10,10,0.4)', color: '#f87171' }}
    >
      {dot('#ef4444')}
      {label}
    </div>

    <div className="flex items-center gap-4" style={{ position: 'absolute', right: 0 }}>
      <div className="flex items-center gap-1.5 text-xs text-zinc-400">
        {dot('#ef4444')}
        Más cercanos
      </div>
      <div className="flex items-center gap-1.5 text-xs text-zinc-400">
        {dot('#C9952A')}
        Resto de monumentos
      </div>
    </div>
  </div>
)
}