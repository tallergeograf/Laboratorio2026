import {
  SudirGeocodeParamsSchema,
  SudirReverseParamsSchema,
  SudirGeocodeResultSchema,
  SudirGeocodeResultsSchema,
  SudirReverseResultSchema,
  SudirReverseResultsSchema,
} from '@/lib/models/sudir'
import type {
  SudirGeocodeParams,
  SudirReverseParams,
  SudirGeocodeResult,
  SudirGeocodeResults,
  SudirReverseResult,
  SudirReverseResults,
} from '@/lib/models/sudir'

export const parseSudirGeocodeParams = (params: SudirGeocodeParams) =>
  SudirGeocodeParamsSchema.parse(params)

export const parseSudirReverseParams = (params: SudirReverseParams) =>
  SudirReverseParamsSchema.parse(params)

export const createSudirGeocodeResultAdapter = (data: unknown): SudirGeocodeResult =>
  SudirGeocodeResultSchema.parse(data)

export const createSudirGeocodeResultsAdapter = (data: unknown): SudirGeocodeResults =>
  SudirGeocodeResultsSchema.parse(data)

export const createSudirReverseResultAdapter = (data: unknown): SudirReverseResult =>
  SudirReverseResultSchema.parse(data)

export const createSudirReverseResultsAdapter = (data: unknown): SudirReverseResults =>
  SudirReverseResultsSchema.parse(data)
