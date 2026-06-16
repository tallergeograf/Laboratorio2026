"use client";

import { X, SlidersHorizontal } from "lucide-react";
import { Button } from "@/components/ui/button";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Separator } from "@/components/ui/separator";
import { cn } from "@/lib/utils";
import { FilterGroup } from "./FilterGroup";
import {
  FILTER_CONFIG,
  FILTER_LABELS,
  Filters,
  FilterKey,
} from "./types";

interface FilterSidebarProps {
  open: boolean;
  onClose: () => void;
  filters: Filters;
  onChange: (key: FilterKey, value: string[]) => void;
  onApply: (filters: Filters) => void;
}

export function FilterSidebar({
  open,
  onClose,
  filters,
  onChange,
  onApply,
}: FilterSidebarProps) {
  const activeCount = Object.values(filters).flat().length;

  const handleClear = () => {
    (Object.keys(FILTER_CONFIG) as FilterKey[]).forEach((key) =>
      onChange(key, [])
    );
  };

  return (
    <>
      {/* Overlay mobile */}
      {open && (
        <div
          className="fixed inset-0 z-20 bg-black/50 lg:hidden"
          onClick={onClose}
        />
      )}

      {/* Panel */}
      <aside
        className={cn(
          "fixed right-0 top-0 z-30 h-full w-72 bg-[#111] border-l border-white/10",
          "flex flex-col transition-transform duration-300 ease-in-out",
          open ? "translate-x-0" : "translate-x-full"
        )}
      >
        {/* Header */}
        <div className="flex items-center justify-between px-4 py-4 border-b border-white/10">
          <div className="flex items-center gap-2">
            <SlidersHorizontal size={14} className="text-white/40" />
            <span className="text-sm font-medium text-white/80">Filtros</span>
            {activeCount > 0 && (
              <span className="text-[10px] bg-blue-500/20 text-blue-400 px-1.5 py-0.5 rounded-full font-mono">
                {activeCount}
              </span>
            )}
          </div>
          <button
            onClick={onClose}
            className="text-white/30 hover:text-white/70 transition-colors"
          >
            <X size={16} />
          </button>
        </div>

        {/* Groups */}
        <ScrollArea className="flex-1 px-4 py-4">
          <div className="space-y-5">
            {(Object.keys(FILTER_CONFIG) as FilterKey[]).map((key, i, arr) => (
              <div key={key}>
                <FilterGroup
                  label={FILTER_LABELS[key]}
                  options={FILTER_CONFIG[key]}
                  selected={filters[key]}
                  onChange={(value) => onChange(key, value)}
                />
                {i < arr.length - 1 && (
                  <Separator className="mt-5 bg-white/5" />
                )}
              </div>
            ))}
          </div>
        </ScrollArea>

        {/* Footer */}
        <div className="px-4 py-4 border-t border-white/10 space-y-2">
          {activeCount > 0 && (
            <button
              onClick={handleClear}
              className="w-full text-xs text-white/30 hover:text-white/60 transition-colors text-center"
            >
              Limpiar filtros
            </button>
          )}
          <Button
            onClick={() => onApply(filters)}
            className="w-full bg-blue-600 hover:bg-blue-500 text-white text-sm font-medium"
          >
            Filtrar
          </Button>
        </div>
      </aside>
    </>
  );
}
