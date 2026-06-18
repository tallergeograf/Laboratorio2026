'use client';

import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';
import { Bar, BarChart, ResponsiveContainer, XAxis, YAxis, Tooltip } from 'recharts';
import { Provider, PROVIDER_COLORS } from '@/app/constants/providers';
import { Cell } from 'recharts';

interface MetricValue {
  provider: Provider;
  value: number;
}

interface MetricChartProps {
  label: string;
  description: string;
  unit: string;
  values: MetricValue[];
}

export function MetricChart({ label, description, unit, values }: MetricChartProps) {
  return (
    <Card className="bg-[#1a1a1a] border-white/10">
      <CardHeader className="pb-2">
        <CardTitle className="text-[15px] font-medium text-[#f0f0f0]">{label}</CardTitle>
        <CardDescription className="text-xs text-white/40">{description} · {unit}</CardDescription>
      </CardHeader>

      <CardContent>
        <div className="flex gap-4 mb-4 flex-wrap">
          {values.map(({ provider }) => (
            <div key={provider} className="flex items-center gap-1.5 text-xs text-white/50">
              <span className="w-2 h-2 rounded-sm" style={{ background: PROVIDER_COLORS[provider] }} />
              {provider}
            </div>
          ))}
        </div>

        <ResponsiveContainer width="100%" height={220}>
          <BarChart data={values}>
            <XAxis dataKey="provider" tick={{ fill: 'rgba(255,255,255,0.4)', fontSize: 12 }} axisLine={false} tickLine={false} />
            <YAxis
  tick={{ fill: 'rgba(255,255,255,0.3)', fontSize: 11 }}
  axisLine={false}
  tickLine={false}
  domain={[0, 100]}
  ticks={[0, 25, 50, 75, 100]}
  tickFormatter={(v) => `${v}%`}
/>
            <Tooltip
              contentStyle={{ background: '#2a2a2a', border: '0.5px solid rgba(255,255,255,0.1)', borderRadius: 8 }}
              labelStyle={{ color: '#f0f0f0' }}
              itemStyle={{ color: 'rgba(255,255,255,0.5)' }}
              formatter={(value) => [`${Number(value ?? 0).toFixed(1)} ${unit}`]}
            />
            <Bar dataKey="value" radius={[6, 6, 0, 0]}>
              {values.map(({ provider }) => (
                <Cell key={provider} fill={PROVIDER_COLORS[provider]} />
              ))}
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}