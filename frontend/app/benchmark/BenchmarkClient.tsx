"use client";
import { useState } from "react";
import { usePathname } from "next/navigation";
import { cn } from "@/lib/utils";
import { FilterSidebar } from "@/components/filters/FilterSidebar";
import { SidebarToggle } from "@/components/filters/SidebarToggle";
import { Filters, FilterKey, DEFAULT_FILTERS } from "@/components/filters/types";
import { FiltersContext } from "./FiltersContext";

const MAP_FILTERS: FilterKey[] = ["demo", "providers", "departments", "category"];
const ANALYSIS_FILTERS: FilterKey[] = ["demo", "providers", "departments", "category", "variacion"];

export function BenchmarkClient({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();
  const showSidebar = pathname !== "/benchmark";
  const isMap = pathname === "/benchmark/map";

  const [open, setOpen] = useState(true);
  const [filters, setFilters] = useState<Filters>(DEFAULT_FILTERS);
  const [appliedFilters, setAppliedFilters] = useState<Filters>(DEFAULT_FILTERS);

  const effectiveFilters: Filters = isMap
    ? { ...appliedFilters, variacion: ["COMUN"] }
    : appliedFilters;

  const handleChange = (key: FilterKey, value: string[]) => {
    setFilters((prev) => ({ ...prev, [key]: value }));
  };

  const handleApply = (current: Filters) => {
    setAppliedFilters(current);
  };

  const activeCount = Object.values(effectiveFilters).flat().length;
  const visibleKeys = isMap ? MAP_FILTERS : ANALYSIS_FILTERS;

  return (
    <FiltersContext.Provider value={effectiveFilters}>
      <div className={cn("relative min-h-svh transition-[padding-right] duration-300", showSidebar && open ? "pr-72" : "")}>
        {children}
        {showSidebar && (
          <>
            <SidebarToggle open={open} onClick={() => setOpen(true)} activeCount={activeCount} />
            <FilterSidebar
              open={open}
              onClose={() => setOpen(false)}
              filters={filters}
              onChange={handleChange}
              onApply={handleApply}
              visibleKeys={visibleKeys}
            />
          </>
        )}
      </div>
    </FiltersContext.Provider>
  );
}