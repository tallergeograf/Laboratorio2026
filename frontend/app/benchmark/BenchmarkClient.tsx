"use client";

import { useState } from "react";
import { cn } from "@/lib/utils";
import { FilterSidebar } from "@/components/filters/FilterSidebar";
import { SidebarToggle } from "@/components/filters/SidebarToggle";
import { Filters, FilterKey, DEFAULT_FILTERS } from "@/components/filters/types";
import { FiltersContext } from "./FiltersContext";

export function BenchmarkClient({ children }: { children: React.ReactNode }) {
  const [open, setOpen] = useState(true);
  const [filters, setFilters] = useState<Filters>(DEFAULT_FILTERS);
  const [appliedFilters, setAppliedFilters] = useState<Filters>(DEFAULT_FILTERS);

  const handleChange = (key: FilterKey, value: string[]) => {
    setFilters((prev) => ({ ...prev, [key]: value }));
  };

  const handleApply = (current: Filters) => {
    setAppliedFilters(current);
  };

  const activeCount = Object.values(appliedFilters).flat().length;

  return (
    <FiltersContext.Provider value={appliedFilters}>
      <div className={cn("relative min-h-svh transition-[padding-right] duration-300", open ? "pr-72" : "")}>
        {children}

        <SidebarToggle
          open={open}
          onClick={() => setOpen(true)}
          activeCount={activeCount}
        />

        <FilterSidebar
          open={open}
          onClose={() => setOpen(false)}
          filters={filters}
          onChange={handleChange}
          onApply={handleApply}
        />
      </div>
    </FiltersContext.Provider>
  );
}
