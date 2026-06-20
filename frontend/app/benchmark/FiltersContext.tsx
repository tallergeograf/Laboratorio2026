"use client";

import { createContext, useContext } from "react";
import { Filters, DEFAULT_FILTERS } from "@/components/filters/types";

export const FiltersContext = createContext<Filters>(DEFAULT_FILTERS);

export function useFilters(): Filters {
  return useContext(FiltersContext);
}
