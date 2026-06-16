"use client";

import { Checkbox } from "@/components/ui/checkbox";
import { Label } from "@/components/ui/label";

interface FilterGroupProps {
  label: string;
  options: readonly string[];
  selected: string[];
  onChange: (value: string[]) => void;
}

export function FilterGroup({ label, options, selected, onChange }: FilterGroupProps) {
  const toggle = (item: string) => {
    onChange(
      selected.includes(item)
        ? selected.filter((s) => s !== item)
        : [...selected, item]
    );
  };

  return (
    <div className="space-y-2">
      <p className="text-[11px] font-semibold uppercase tracking-widest text-white/30">
        {label}
      </p>
      <div className="space-y-1.5">
        {options.map((option) => (
          <div key={option} className="flex items-center gap-2">
            <Checkbox
              id={option}
              checked={selected.includes(option)}
              onCheckedChange={() => toggle(option)}
              className="border-white/20 data-[state=checked]:bg-blue-500 data-[state=checked]:border-blue-500"
            />
            <Label
              htmlFor={option}
              className="text-xs text-white/60 cursor-pointer hover:text-white/90 transition-colors font-mono"
            >
              {option}
            </Label>
          </div>
        ))}
      </div>
    </div>
  );
}
