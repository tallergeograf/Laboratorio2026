"use client";

import { createContext, useContext } from "react";
import { Filters, DEFAULT_FILTERS } from "@/components/filters/types";

export type FiltersContextValue = {
  applied: Filters;
  live: Filters;
};

const defaultValue: FiltersContextValue = { applied: DEFAULT_FILTERS, live: DEFAULT_FILTERS };

export const FiltersContext = createContext<FiltersContextValue>(defaultValue);

export function useFilters(): Filters {
  return useContext(FiltersContext).applied;
}

export function useLiveFilters(): Filters {
  return useContext(FiltersContext).live;
}
