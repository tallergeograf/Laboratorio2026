'use client';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';
import { Bar, BarChart, ResponsiveContainer, XAxis, YAxis, Tooltip, Legend } from 'recharts';
import { Provider } from '@/app/constants/providers';

interface GroupedMetricChartProps {
  label: string;
  description: string;
  unit: string;
  providers: Provider[];
  ruralValues: number[];
  urbanValues: number[];
}

export function GroupedMetricChart({ label, description, unit, providers, ruralValues, urbanValues }: GroupedMetricChartProps) {
  const data = providers.map((provider, i) => ({
    provider,
    Rural: ruralValues[i],
    Urbano: urbanValues[i],
  }));

  return (
    <Card className="bg-[#1a1a1a] border-white/10 col-span-2">
      <CardHeader className="pb-2">
        <CardTitle className="text-[15px] font-medium text-[#f0f0f0]">{label}</CardTitle>
        <CardDescription className="text-xs text-white/40">{description} · {unit}</CardDescription>
      </CardHeader>
      <CardContent>
        <ResponsiveContainer width="100%" height={280}>
          <BarChart data={data} barCategoryGap="15%">
            <XAxis
              dataKey="provider"
              tick={{ fill: 'rgba(255,255,255,0.4)', fontSize: 12 }}
              axisLine={false}
              tickLine={false}
            />
            <YAxis
              tick={{ fill: 'rgba(255,255,255,0.3)', fontSize: 11 }}
              axisLine={false}
              tickLine={false}
              domain={[0, 100]}
              ticks={[0, 25, 50, 75, 100]}
              tickFormatter={(v) => `${v}%`}
            />
            <Tooltip
              cursor={false}
              contentStyle={{ background: '#2a2a2a', border: '0.5px solid rgba(255,255,255,0.1)', borderRadius: 8 }}
              labelStyle={{ color: '#f0f0f0' }}
              itemStyle={{ color: 'rgba(255,255,255,0.5)' }}
              formatter={(value) => [`${Number(value ?? 0).toFixed(1)}%`]}
            />
            <Legend wrapperStyle={{ color: 'rgba(255,255,255,0.4)', fontSize: 12 }} />
            <Bar dataKey="Rural" fill="#f97316" radius={[4, 4, 0, 0]} />
            <Bar dataKey="Urbano" fill="#a78bfa" radius={[4, 4, 0, 0]} />
          </BarChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}