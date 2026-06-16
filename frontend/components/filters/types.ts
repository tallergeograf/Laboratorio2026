export const FILTER_CONFIG = {
  providers: ["Photon", "Sudir", "Nominatim"],
  departments: ["MONTEVIDEO", "CANELONES", "MALDONADO"],
  category: [
    "CALLE_NUMERO",
    "CALLE_NUMERO_LOCALIDAD",
    "CALLE_NUMERO_DEPARTAMENTO",
    "CALLE_NUMERO_LOCALIDAD_DEPARTAMENTO",
    "RUTA_KILOMETRO",
    "SOLAR_MANZANA",
    "PUNTO_DE_INTERES",
  ],
  variacion: [
    "COMUN",
    "ABREVIACION",
    "PERMUTACION",
    "ERR1_S",
    "ERR1_B",
    "ERR2_SS",
    "ERR2_SB",
    "ERR2_BS",
    "ERR2_BB",
  ],
} as const;

export const FILTER_LABELS: Record<keyof typeof FILTER_CONFIG, string> = {
  providers:   "Proveedor",
  departments: "Departamento",
  category:    "Categoría",
  variacion:   "Variación",
};

export type FilterKey = keyof typeof FILTER_CONFIG;

export type Filters = {
  [K in FilterKey]: string[];
};

export const DEFAULT_FILTERS: Filters = {
  providers:   [],
  departments: [],
  category:    [],
  variacion:   [],
};
