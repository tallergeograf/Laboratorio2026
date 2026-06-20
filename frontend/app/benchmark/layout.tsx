import type { Metadata } from "next";
import { BenchmarkClient } from "./BenchmarkClient";

export const metadata: Metadata = {
  title: "Laboratorio 2026",
  description: "Benchmark de geocoders y casos de estudio",
};

export default function BenchmarkLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return <BenchmarkClient>{children}</BenchmarkClient>;
}
