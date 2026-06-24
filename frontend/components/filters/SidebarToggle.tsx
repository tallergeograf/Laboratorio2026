"use client";

import { ChevronRight } from "lucide-react";
import { cn } from "@/lib/utils";

interface SidebarToggleProps {
  open: boolean;
  onClick: () => void;
  activeCount: number;
}

export function SidebarToggle({ open, onClick, activeCount }: SidebarToggleProps) {
  return (
    <button
      onClick={onClick}
      className={cn(
        "fixed right-0 top-1/2 -translate-y-1/2 z-40",
        "flex items-center gap-1.5 px-2 py-3",
        "bg-[#1a1a1a] border border-white/10 border-r-0 rounded-l-lg",
        "text-white/40 hover:text-white/80 transition-colors",
        open && "opacity-0 pointer-events-none"
      )}
    >
      {activeCount > 0 && (
        <span className="text-[9px] text-blue-400 font-mono">{activeCount}</span>
      )}
      <ChevronRight size={14} />
    </button>
  );
}
