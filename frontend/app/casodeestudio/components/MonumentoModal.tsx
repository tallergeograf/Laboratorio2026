'use client'

import type { Monumento } from '@/lib/models/monumentos'
import type { WikipediaSummary } from '@/lib/wikipedia'
import XIcon from '@/lib/icons/XIcon'

type MonumentoModalProps = {
  monumento: Monumento
  wikiData: WikipediaSummary | null
  loading: boolean
  onClose: () => void
}

export default function MonumentoModal({ monumento, wikiData, loading, onClose }: MonumentoModalProps) {
  return (
    <div
      className="fixed inset-0 z-[9999] flex items-center justify-center bg-black/50 p-4 cursor-pointer"
      onClick={onClose}
    >
      <div
        className="relative w-full max-w-sm rounded-xl bg-white dark:bg-zinc-900 shadow-2xl overflow-hidden cursor-default"
        onClick={(e) => e.stopPropagation()}
      >
        <button
          onClick={onClose}
          className="absolute top-3 right-3 flex items-center justify-center rounded-full bg-zinc-100 dark:bg-zinc-800 text-zinc-500 hover:bg-zinc-200 dark:hover:bg-zinc-700 transition-colors cursor-pointer p-1 m-0"
          aria-label="Cerrar"
        >
          <XIcon size={24} />
        </button>

        {loading && (
          <div className="flex h-40 items-center justify-center">
            <div className="h-8 w-8 rounded-full border-4 border-zinc-200 dark:border-zinc-700 border-t-blue-500 animate-spin" />
          </div>
        )}

        {!loading && wikiData?.thumbnail && (
          // eslint-disable-next-line @next/next/no-img-element
          <img
            src={wikiData.thumbnail.source}
            alt={monumento.nombre}
            className="w-full h-48 object-cover"
          />
        )}

        <div className="p-4 space-y-2">
          <h2 className="font-semibold text-zinc-900 dark:text-zinc-100 text-sm leading-snug pr-6">
            {monumento.nombre}
          </h2>

          {(monumento.direccion || monumento.localidad) && (
            <p className="text-xs text-zinc-500 dark:text-zinc-400">
              {[monumento.direccion, monumento.localidad].filter(Boolean).join(', ')}
            </p>
          )}

          {!loading && wikiData?.extract && (
            <p className="text-xs text-zinc-600 dark:text-zinc-300 leading-relaxed line-clamp-5">
              {wikiData.extract}
            </p>
          )}
        </div>
      </div>
    </div>
  )
}
