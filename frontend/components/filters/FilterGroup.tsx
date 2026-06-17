"use client";
import { useState } from "react";
import { ChevronDown } from "lucide-react";
import { cn } from "@/lib/utils";

function formatLabel(s: string) {
  return s.replace(/_/g, " ").toLowerCase().replace(/^\w/, (c) => c.toUpperCase());
}

interface FilterGroupProps {
  label: string;
  options: readonly string[];
  selected: string[];
  onChange: (value: string[]) => void;
}

export function FilterGroup({ label, options, selected, onChange }: FilterGroupProps) {
  const [open, setOpen] = useState(true);

  const toggle = (item: string) => {
    onChange(
      selected.includes(item)
        ? selected.filter((s) => s !== item)
        : [...selected, item]
    );
  };

  const clearGroup = () => onChange([]);

  return (
    <div>
      <button
        onClick={() => setOpen((p) => !p)}
        className="w-full flex items-center justify-between py-1 group"
      >
        <div className="flex items-center gap-2">
          <span className="text-[11px] font-semibold uppercase tracking-widest text-white/30 group-hover:text-white/50 transition-colors">
            {label}
          </span>
          {selected.length > 0 && (
            <span className="text-[9px] bg-blue-500/20 text-blue-400 px-1.5 py-0.5 rounded-full font-mono">
              {selected.length}
            </span>
          )}
        </div>
        <ChevronDown
          size={13}
          className={cn(
            "text-white/20 transition-transform duration-200",
            open && "rotate-180"
          )}
        />
      </button>

      <div className={cn(
        "overflow-hidden transition-all duration-200",
        open ? "max-h-[600px] opacity-100 mt-2" : "max-h-0 opacity-0"
      )}>
        {selected.length > 0 && (
          <div className="flex flex-wrap gap-1 mb-2">
            {selected.map((s) => (
              <button
                key={s}
                onClick={() => toggle(s)}
                className="flex items-center gap-1 text-[10px] bg-blue-500/15 text-blue-400 px-2 py-0.5 rounded-full hover:bg-blue-500/25 transition-colors"
              >
                {formatLabel(s)}
                <span className="text-blue-300/60">×</span>
              </button>
            ))}
            <button
              onClick={clearGroup}
              className="text-[10px] text-white/20 hover:text-white/50 transition-colors px-1"
            >
              limpiar
            </button>
          </div>
        )}

        <div className="space-y-0.5">
          {options.map((option) => {
            const isSelected = selected.includes(option);
            return (
              <button
                key={option}
                onClick={() => toggle(option)}
                className={cn(
                  "w-full flex items-center gap-2.5 px-2 py-1.5 rounded-md text-left transition-colors",
                  isSelected
                    ? "bg-blue-500/10 text-white/90"
                    : "text-white/40 hover:text-white/70 hover:bg-white/5"
                )}
              >
                <div className={cn(
                  "w-3.5 h-3.5 rounded-sm border flex items-center justify-center flex-shrink-0 transition-colors",
                  isSelected
                    ? "bg-blue-500 border-blue-500"
                    : "border-white/20"
                )}>
                  {isSelected && (
                    <svg width="8" height="6" viewBox="0 0 8 6" fill="none">
                      <path d="M1 3L3 5L7 1" stroke="white" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
                    </svg>
                  )}
                </div>
                <span className="text-xs">{formatLabel(option)}</span>
              </button>
            );
          })}
        </div>
      </div>
    </div>
  );
}