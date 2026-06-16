'use client';

import { ChartContainer, type ChartConfig } from "@/components/ui/chart"
import { Bar, BarChart, CartesianGrid, Cell, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts"
import { sampleAccuracyData } from "./data"

const averageError = sampleAccuracyData.find(d => d.metric === 'averageError')?.values || []

const chartConfig: ChartConfig = {
}

const providerColors: Record<string, string> = {
  'Provider A': '#8884d8',
  'Provider B': '#82ca9d',
  'Provider C': '#ffc658',
}

export default function AccuracyChart() {
  return (
    <ResponsiveContainer width="25%" height={300}>
      <BarChart data={averageError}>
        <XAxis dataKey="provider" />
        <YAxis />
        <Tooltip />
        <Bar dataKey="value">
          {averageError.map(({ provider }) => (
            <Cell key={provider} fill={providerColors[provider]} />
          ))}
        </Bar>
      </BarChart>
    </ResponsiveContainer>
  )
}
    